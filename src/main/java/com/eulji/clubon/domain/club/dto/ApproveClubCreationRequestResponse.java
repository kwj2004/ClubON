package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubCreationRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ApproveClubCreationRequestResponse(
    Long requestId,
    String status,
    Long createdClubId,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime reviewedAt
) {

    public static ApproveClubCreationRequestResponse from(ClubCreationRequest request) {
        return new ApproveClubCreationRequestResponse(
            request.getId(),
            request.getStatus().name(),
            request.getCreatedClub().getId(),
            request.getReviewedAt()
        );
    }
}
