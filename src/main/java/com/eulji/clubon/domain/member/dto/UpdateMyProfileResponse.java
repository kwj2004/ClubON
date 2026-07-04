package com.eulji.clubon.domain.member.dto;

import com.eulji.clubon.domain.member.entity.Member;

import java.time.LocalDateTime;

public record UpdateMyProfileResponse(
        Long userid,
        String email,
        String name,
        String studentid,
        String department,
        String role,
        LocalDateTime updatedAt
) {

    public static UpdateMyProfileResponse from(Member member) {
        return new UpdateMyProfileResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getStudentId(),
                member.getDepartment(),
                member.getRole().name(),
                member.getUpdatedAt()
        );
    }
}
