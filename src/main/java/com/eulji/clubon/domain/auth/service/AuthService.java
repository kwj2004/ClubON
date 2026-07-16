package com.eulji.clubon.domain.auth.service;

import com.eulji.clubon.domain.auth.dto.LoginRequest;
import com.eulji.clubon.domain.auth.dto.LoginResponse;
import com.eulji.clubon.domain.auth.dto.SignupMemberType;
import com.eulji.clubon.domain.auth.dto.SignupRequest;
import com.eulji.clubon.domain.auth.dto.SignupResponse;
import com.eulji.clubon.domain.auth.dto.TokenInfo;
import com.eulji.clubon.domain.auth.dto.RefreshTokenRequest;
import com.eulji.clubon.domain.auth.entity.RefreshToken;
import com.eulji.clubon.domain.auth.repository.RefreshTokenRepository;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubAdminRequest;
import com.eulji.clubon.domain.club.repository.ClubAdminRequestRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.domain.department.service.DepartmentService;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.entity.Role;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.ClubNotFoundException;
import com.eulji.clubon.global.error.DuplicateEmailException;
import com.eulji.clubon.global.error.DuplicateStudentIdException;
import com.eulji.clubon.global.error.LoginFailedException;
import com.eulji.clubon.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

// 회원가입, 운영자 신청 회원가입, 로그인을 처리하는 인증 서비스입니다.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final DepartmentService departmentService;
    private final EmailVerificationService emailVerificationService;
    private final ClubRepository clubRepository;
    private final ClubAdminRequestRepository clubAdminRequestRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${auth.refresh-token.expiration}")
    private long refreshTokenExpiration;

    // 학교 이메일 인증 여부와 중복 정보를 검증한 뒤 회원을 생성합니다.
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        validateDuplicateMember(request);
        emailVerificationService.validateVerifiedEmail(request.email());
        departmentService.validateActiveDepartment(request.department());

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .studentId(request.studentId())
                .department(request.department())
                .role(Role.ROLE_STUDENT)
                .build();

        Member savedMember = memberRepository.save(member);
        ClubAdminRequest clubAdminRequest = createClubAdminRequestIfNeeded(savedMember, request);

        return SignupResponse.of(savedMember, clubAdminRequest);
    }

    // 로그인 정보가 맞으면 JWT 토큰 응답을 생성합니다.
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginFailedException();
        }

        TokenInfo tokenInfo = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        if (request.rememberMe()) tokenInfo = addRefreshToken(member, tokenInfo);
        return LoginResponse.of(member, tokenInfo);
    }

    @Transactional
    public TokenInfo refresh(RefreshTokenRequest request) {
        RefreshToken saved = refreshTokenRepository.findByTokenHash(hash(request.refreshToken()))
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));
        if (!saved.isUsable(LocalDateTime.now()))
            throw new IllegalArgumentException("Refresh Token이 만료되었거나 폐기되었습니다.");
        saved.revoke();
        return addRefreshToken(saved.getMember(), jwtTokenProvider.createToken(saved.getMember().getEmail(), saved.getMember().getRole()));
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByTokenHash(hash(request.refreshToken())).ifPresent(RefreshToken::revoke);
    }

    private TokenInfo addRefreshToken(Member member, TokenInfo accessToken) {
        byte[] bytes = new byte[32]; secureRandom.nextBytes(bytes);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        refreshTokenRepository.save(RefreshToken.builder().member(member).tokenHash(hash(raw))
            .expiresAt(LocalDateTime.now().plusNanos(refreshTokenExpiration * 1_000_000)).build());
        return accessToken.withRefreshToken(raw, refreshTokenExpiration / 1000);
    }

    private String hash(String value) {
        try { return java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }

    // 이메일과 학번은 서비스 전체에서 유일해야 하므로 가입 전에 중복을 차단합니다.
    private void validateDuplicateMember(SignupRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }

        if (memberRepository.existsByStudentId(request.studentId())) {
            throw new DuplicateStudentIdException();
        }
    }

    // 회원 유형이 CLUB_ADMIN이면 권한을 바로 주지 않고 운영자 신청을 PENDING 상태로 저장합니다.
    private ClubAdminRequest createClubAdminRequestIfNeeded(Member member, SignupRequest request) {
        SignupMemberType memberType = request.memberType() == null
                ? SignupMemberType.STUDENT
                : request.memberType();

        if (memberType != SignupMemberType.CLUB_ADMIN) {
            return null;
        }

        if (request.clubAdminRequest() == null) {
            throw new IllegalArgumentException("운영자 신청 정보를 입력해주세요.");
        }

        Club club = clubRepository.findById(request.clubAdminRequest().clubId())
                .orElseThrow(ClubNotFoundException::new);

        ClubAdminRequest clubAdminRequest = ClubAdminRequest.builder()
                .member(member)
                .club(club)
                .position(request.clubAdminRequest().position())
                .build();

        return clubAdminRequestRepository.save(clubAdminRequest);
    }
}
