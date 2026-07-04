package com.eulji.clubon.domain.auth.dto;

import com.eulji.clubon.domain.member.entity.Member;

import java.time.LocalDateTime;

public record SignupResponse(
        Long userId,
        String email,
        String name,
        String studentId,
        String department,
        String role,
        LocalDateTime createdAt
) {

    public static SignupResponse from(Member member) {
        return new SignupResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getStudentId(),
                member.getDepartment(),
                member.getRole().name(),
                member.getCreatedAt()
        );
    }
}
