package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;

public record BookmarkedClubResponse(
        Long clubId,
        String name,
        String type,
        String status,
        String shortDescription,
        String imageUrl
) {

    public static BookmarkedClubResponse from(Club club) {
        return new BookmarkedClubResponse(
                club.getId(),
                club.getName(),
                club.getType().name(),
                club.getStatus().name(),
                club.getShortDescription(),
                club.getImageUrl()
        );
    }
}
