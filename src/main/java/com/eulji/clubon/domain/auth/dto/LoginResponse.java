package com.eulji.clubon.domain.auth.dto;

import com.eulji.clubon.domain.member.entity.Member;

public record LoginResponse(
        Long userId,
        String name,
        String studentId,
        String department,
        String role,
        TokenInfo tokenInfo
) {

    public static LoginResponse of(Member member, TokenInfo tokenInfo) {
        return new LoginResponse(
                member.getId(),
                member.getName(),
                member.getStudentId(),
                member.getDepartment(),
                member.getRole().name(),
                tokenInfo
        );
    }
}
