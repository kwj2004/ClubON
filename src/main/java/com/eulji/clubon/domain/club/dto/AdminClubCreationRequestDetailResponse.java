package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubCreationRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AdminClubCreationRequestDetailResponse(
    Long requestId,
    Long requesterId,
    String requesterName,
    String name,
    String type,
    String category,
    String shortDescription,
    String fullDescription,
    String status,
    String rejectedReason,
    Long createdClubId,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime reviewedAt
) {

    public static AdminClubCreationRequestDetailResponse from(ClubCreationRequest request) {
        Club createdClub = request.getCreatedClub();

        return new AdminClubCreationRequestDetailResponse(
            request.getId(),
            request.getRequester().getId(),
            request.getRequester().getName(),
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
