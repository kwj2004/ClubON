package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubApplication;
import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;

import java.time.LocalDateTime;

public record MyClubApplicationResponse(
    Long applicationId,
    Long clubId,
    String clubName,
    ClubApplicationStatus status,
    LocalDateTime createdAt
) {

    public static MyClubApplicationResponse from(ClubApplication application) {
        return new MyClubApplicationResponse(
            application.getId(),
            application.getClub().getId(),
            application.getClub().getName(),
            application.getStatus(),
            application.getAppliedAt()
        );
    }
}
