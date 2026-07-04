package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubCreationRequest;

public record CreateClubResponse(
        Long requestId,
        String status
) {

    public static CreateClubResponse from(ClubCreationRequest request) {
        return new CreateClubResponse(
                request.getId(),
                request.getStatus().name()
        );
    }
}
