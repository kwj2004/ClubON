package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubMembership;

import java.time.LocalDateTime;

public record MyClubResponse(
        Long clubId,
        String name,
        String type,
        String shortDescription,
        String myRole,
        LocalDateTime joinedAt
) {

    public static MyClubResponse from(ClubMembership membership) {
        return new MyClubResponse(
                membership.getClub().getId(),
                membership.getClub().getName(),
                membership.getClub().getType().name(),
                membership.getClub().getShortDescription(),
                membership.getRole().name(),
                membership.getJoinedAt()
        );
    }
}
