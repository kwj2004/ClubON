package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubCreationRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record RejectClubCreationRequestResponse(
    Long requestId,
    String status,
    String rejectedReason,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime reviewedAt
) {

    public static RejectClubCreationRequestResponse from(ClubCreationRequest request) {
        return new RejectClubCreationRequestResponse(
            request.getId(),
            request.getStatus().name(),
            request.getRejectedReason(),
            request.getReviewedAt()
        );
    }
}
