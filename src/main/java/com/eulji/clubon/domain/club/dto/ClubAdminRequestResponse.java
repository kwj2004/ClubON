package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubAdminRequest;

import java.time.LocalDateTime;

public record ClubAdminRequestResponse(
    Long requestId,
    Long memberId,
    String memberName,
    String memberEmail,
    Long clubId,
    String clubName,
    String position,
    String status,
    String rejectedReason,
    LocalDateTime createdAt,
    LocalDateTime reviewedAt
) {
    public static ClubAdminRequestResponse from(ClubAdminRequest request) {
        return new ClubAdminRequestResponse(
            request.getId(),
            request.getMember().getId(),
            request.getMember().getName(),
            request.getMember().getEmail(),
            request.getClub().getId(),
            request.getClub().getName(),
            request.getPosition(),
            request.getStatus().name(),
            request.getRejectedReason(),
            request.getCreatedAt(),
            request.getReviewedAt()
        );
    }
}
