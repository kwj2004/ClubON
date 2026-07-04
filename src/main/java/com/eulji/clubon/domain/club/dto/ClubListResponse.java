package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;

public record ClubListResponse(
        Long clubId,
        String name,
        String type,
        String status,
        String shortDescription,
        String imageUrl
) {

    public static ClubListResponse from(Club club) {
        return new ClubListResponse(
                club.getId(),
                club.getName(),
                club.getType().name(),
                club.getStatus().name(),
                club.getShortDescription(),
                club.getImageUrl()
        );
    }
}
