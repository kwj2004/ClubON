package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubApplication;
import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ClubApplicationApplicantResponse(
    Long applicationId,
    String studentName,
    String studentId,
    String department,
    String email,
    String content,
    ClubApplicationStatus status,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt
) {

    public static ClubApplicationApplicantResponse from(ClubApplication application) {
        return new ClubApplicationApplicantResponse(
            application.getId(),
            application.getMember().getName(),
            application.getMember().getStudentId(),
            application.getMember().getDepartment(),
            application.getMember().getEmail(),
            application.getContent(),
            application.getStatus(),
            application.getAppliedAt()
        );
    }
}
