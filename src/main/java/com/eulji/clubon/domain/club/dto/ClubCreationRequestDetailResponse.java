package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubCreationRequest;

import java.time.LocalDateTime;

public record ClubCreationRequestDetailResponse(
        Long requestId,
        String name,
        String type,
        String category,
        String shortDescription,
        String fullDescription,
        String status,
        String rejectedReason,
        Long createdClubId,
        LocalDateTime createdAt,
        LocalDateTime reviewedAt
) {

    public static ClubCreationRequestDetailResponse from(ClubCreationRequest request) {
        Club createdClub = request.getCreatedClub();

        return new ClubCreationRequestDetailResponse(
                request.getId(),
                request.getName(),
                request.getType().name(),
                request.getCategory().name(),
                request.getShortDescription(),
                request.getFullDescription(),
                request.getStatus().name(),
                request.getRejectedReason(),
                createdClub == null ? null : createdClub.getId(),
                request.getCreatedAt(),
                request.getReviewedAt()
        );
    }
}
