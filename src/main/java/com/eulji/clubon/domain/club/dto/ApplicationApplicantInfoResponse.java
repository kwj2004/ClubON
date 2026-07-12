package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.member.entity.Member;

public record ApplicationApplicantInfoResponse(
    String name,
    String studentId,
    String department,
    String email
) {
    public static ApplicationApplicantInfoResponse from(Member member) {
        return new ApplicationApplicantInfoResponse(
            member.getName(),
            member.getStudentId(),
            member.getDepartment(),
            member.getEmail()
        );
    }
}
