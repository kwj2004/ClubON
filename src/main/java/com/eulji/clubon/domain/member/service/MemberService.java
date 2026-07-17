package com.eulji.clubon.domain.member.service;

import com.eulji.clubon.domain.department.service.DepartmentService;
import com.eulji.clubon.domain.member.dto.MyProfileResponse;
import com.eulji.clubon.domain.member.dto.UpdateMyProfileRequest;
import com.eulji.clubon.domain.member.dto.UpdateMyProfileResponse;
import com.eulji.clubon.domain.member.dto.WithdrawMemberRequest;
import com.eulji.clubon.domain.auth.repository.EmailVerificationRepository;
import com.eulji.clubon.domain.auth.repository.PasswordResetRepository;
import com.eulji.clubon.domain.auth.repository.RefreshTokenRepository;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.global.error.InvalidPasswordException;
import com.eulji.clubon.global.error.LastClubAdminException;
import com.eulji.clubon.global.error.MemberNotFoundException;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentService departmentService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ClubMembershipRepository clubMembershipRepository;

    public MyProfileResponse getMyProfile(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        return MyProfileResponse.from(member);
    }

    @Transactional
    public UpdateMyProfileResponse updateMyProfile(String email, UpdateMyProfileRequest request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        if (request.department() != null) {
            departmentService.validateActiveDepartment(request.department());
        }

        String encodedPassword = request.password() == null ? null : passwordEncoder.encode(request.password());
        member.updateProfile(encodedPassword, request.name(), request.department());

        return UpdateMyProfileResponse.from(member);
    }

    @Transactional
    public void withdraw(String email, WithdrawMemberRequest request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (!member.isActive()) {
            throw new MemberNotFoundException();
        }
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new InvalidPasswordException();
        }

        boolean isLastAdmin = clubMembershipRepository
                .findAllByMemberAndRole(member, ClubMemberRole.ADMIN)
                .stream()
                .anyMatch(membership -> clubMembershipRepository
                        .countByClub_IdAndRole(membership.getClub().getId(), ClubMemberRole.ADMIN) <= 1);
        if (isLastAdmin) {
            throw new LastClubAdminException();
        }

        String originalEmail = member.getEmail();
        String uniqueKey = member.getId() + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String anonymizedEmail = "withdrawn-" + uniqueKey + "@invalid.local";
        String anonymizedStudentId = ("W" + uniqueKey).substring(0, 10);

        refreshTokenRepository.deleteAllByMember(member);
        passwordResetRepository.deleteAllByMember(member);
        emailVerificationRepository.deleteAllByEmail(originalEmail);
        clubMembershipRepository.deleteAllByMember(member);
        member.withdraw(
                anonymizedEmail,
                anonymizedStudentId,
                passwordEncoder.encode(UUID.randomUUID().toString())
        );
    }
}
