package com.eulji.clubon.domain.member.service;

import com.eulji.clubon.domain.department.service.DepartmentService;
import com.eulji.clubon.domain.member.dto.MyProfileResponse;
import com.eulji.clubon.domain.member.dto.UpdateMyProfileRequest;
import com.eulji.clubon.domain.member.dto.UpdateMyProfileResponse;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentService departmentService;

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
}
