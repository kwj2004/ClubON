package com.eulji.clubon.domain.auth.dto;

import com.eulji.clubon.domain.club.entity.ClubAdminRequest;
import com.eulji.clubon.domain.member.entity.Member;

import java.time.LocalDateTime;

// 회원가입 결과와 운영자 신청 생성 여부를 프론트에 전달합니다.
public record SignupResponse(
    Long userId,
    String email,
    String name,
    String studentId,
    String department,
    String role,
    Long clubAdminRequestId,
    String clubAdminRequestStatus,
    LocalDateTime createdAt
) {

    public static SignupResponse from(Member member) {
        return of(member, null);
    }

    public static SignupResponse of(Member member, ClubAdminRequest clubAdminRequest) {
        return new SignupResponse(
            member.getId(),
            member.getEmail(),
            member.getName(),
            member.getStudentId(),
            member.getDepartment(),
            member.getRole().name(),
            clubAdminRequest == null ? null : clubAdminRequest.getId(),
            clubAdminRequest == null ? null : clubAdminRequest.getStatus().name(),
            member.getCreatedAt()
        );
    }
}
