package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubApplication;
import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;

public record CreateClubApplicationResponse(
    Long applicationId,
    ClubApplicationStatus status
) {

    public static CreateClubApplicationResponse from(ClubApplication application) {
        return new CreateClubApplicationResponse(
            application.getId(),
            application.getStatus()
        );
    }
}
