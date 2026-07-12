package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AdminClubListResponse(
    Long clubId,
    String name,
    String type,
    String category,
    boolean isActive,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt
) {

    public static AdminClubListResponse from(Club club) {
        return new AdminClubListResponse(
            club.getId(),
            club.getName(),
            club.getType().name(),
            club.getCategory().name(),
            club.getStatus() == ClubStatus.OPEN,
            club.getCreatedAt()
        );
    }
}
