package com.eulji.clubon.domain.member.dto;

import com.eulji.clubon.domain.member.entity.Member;

import java.time.LocalDateTime;

public record MyProfileResponse(
        Long userid,
        String email,
        String name,
        String studentid,
        String department,
        String role,
        LocalDateTime createdAt
) {

    public static MyProfileResponse from(Member member) {
        return new MyProfileResponse(
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
