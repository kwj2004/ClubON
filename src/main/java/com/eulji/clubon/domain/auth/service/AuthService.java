package com.eulji.clubon.domain.auth.service;

import com.eulji.clubon.domain.auth.dto.LoginRequest;
import com.eulji.clubon.domain.auth.dto.LoginResponse;
import com.eulji.clubon.domain.auth.dto.SignupRequest;
import com.eulji.clubon.domain.auth.dto.SignupResponse;
import com.eulji.clubon.domain.auth.dto.TokenInfo;
import com.eulji.clubon.domain.department.service.DepartmentService;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.entity.Role;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.DuplicateEmailException;
import com.eulji.clubon.global.error.DuplicateStudentIdException;
import com.eulji.clubon.global.error.LoginFailedException;
import com.eulji.clubon.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final DepartmentService departmentService;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        validateDuplicateMember(request);
        departmentService.validateActiveDepartment(request.department());

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .studentId(request.studentId())
                .department(request.department())
                .role(Role.ROLE_STUDENT)
                .build();

        return SignupResponse.from(memberRepository.save(member));
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new LoginFailedException();
        }

        TokenInfo tokenInfo = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        return LoginResponse.of(member, tokenInfo);
    }

    private void validateDuplicateMember(SignupRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException();
        }

        if (memberRepository.existsByStudentId(request.studentId())) {
            throw new DuplicateStudentIdException();
        }
    }

}
