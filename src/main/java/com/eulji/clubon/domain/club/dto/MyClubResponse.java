package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubMembership;

import java.time.LocalDateTime;

public record MyClubResponse(
        Long clubId,
        String name,
        String type,
        String category,
        String shortDescription,
        String imageUrl,
        String myRole,
        LocalDateTime joinedAt
) {

    public static MyClubResponse from(ClubMembership membership) {
        return new MyClubResponse(
                membership.getClub().getId(),
                membership.getClub().getName(),
                membership.getClub().getType().name(),
                membership.getClub().getCategory().name(),
                membership.getClub().getShortDescription(),
                membership.getClub().getImageUrl(),
                membership.getRole().name(),
                membership.getJoinedAt()
        );
    }
}
