package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ClubDetailResponse(
        Long clubId,
        String name,
        String type,
        String status,
        String shortDescription,
        String fullDescription,
        String recruitPeriod,
        String recruitCondition,
        String activityInfo,
        String contactUrl,
        @JsonProperty("isBookmarked")
        boolean isBookmarked
) {

    public static ClubDetailResponse of(Club club, boolean isBookmarked) {
        return new ClubDetailResponse(
                club.getId(),
                club.getName(),
                club.getType().name(),
                club.getStatus().name(),
                club.getShortDescription(),
                club.getFullDescription(),
                club.getRecruitPeriod(),
                club.getRecruitCondition(),
                club.getActivityInfo(),
                club.getContactUrl(),
                isBookmarked
        );
    }
}
