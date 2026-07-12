package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubCreationRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AdminClubCreationRequestListResponse(
    Long requestId,
    Long requesterId,
    String requesterName,
    String name,
    String type,
    String shortDescription,
    String status,
    Long createdClubId,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime reviewedAt
) {

    public static AdminClubCreationRequestListResponse from(ClubCreationRequest request) {
        Club createdClub = request.getCreatedClub();

        return new AdminClubCreationRequestListResponse(
            request.getId(),
            request.getRequester().getId(),
            request.getRequester().getName(),
            request.getName(),
            request.getType().name(),
            request.getShortDescription(),
            request.getStatus().name(),
            createdClub == null ? null : createdClub.getId(),
            request.getCreatedAt(),
            request.getReviewedAt()
        );
    }
}
