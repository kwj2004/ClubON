package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubCreationRequest;

import java.time.LocalDateTime;

public record ClubCreationRequestListResponse(
        Long requestId,
        String name,
        String type,
        String shortDescription,
        String status,
        String rejectedReason,
        Long createdClubId,
        LocalDateTime createdAt,
        LocalDateTime reviewedAt
) {

    public static ClubCreationRequestListResponse from(ClubCreationRequest request) {
        Club createdClub = request.getCreatedClub();

        return new ClubCreationRequestListResponse(
                request.getId(),
                request.getName(),
                request.getType().name(),
                request.getShortDescription(),
                request.getStatus().name(),
                request.getRejectedReason(),
                createdClub == null ? null : createdClub.getId(),
                request.getCreatedAt(),
                request.getReviewedAt()
        );
    }
}
