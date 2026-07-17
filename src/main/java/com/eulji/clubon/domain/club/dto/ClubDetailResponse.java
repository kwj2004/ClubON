package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.util.RecruitmentStatusResolver;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ClubDetailResponse(
        Long clubId,
        String name,
        String type,
        String category,
        String status,
        String shortDescription,
        String fullDescription,
        String recruitPeriod,
        String recruitCondition,
        String activityInfo,
        String contactUrl,
        String imageUrl,
        String recruitmentStatus,
        String recruitmentStatusLabel,
        @JsonProperty("isRecruiting")
        boolean isRecruiting,
        @JsonProperty("isBookmarked")
        boolean isBookmarked,
        String operatorName,
        String operatorEmail
) {

    public static ClubDetailResponse of(
        Club club,
        boolean isBookmarked,
        String operatorName,
        String operatorEmail
    ) {
        RecruitmentStatusInfo recruitmentStatusInfo = RecruitmentStatusResolver.resolve(
                club.getStatus(),
                club.getRecruitPeriod()
        );

        return new ClubDetailResponse(
                club.getId(),
                club.getName(),
                club.getType().name(),
                club.getCategory().name(),
                club.getStatus().name(),
                club.getShortDescription(),
                club.getFullDescription(),
                club.getRecruitPeriod(),
                club.getRecruitCondition(),
                club.getActivityInfo(),
                club.getContactUrl(),
                club.getImageUrl(),
                recruitmentStatusInfo.status(),
                recruitmentStatusInfo.label(),
                recruitmentStatusInfo.isRecruiting(),
                isBookmarked,
                operatorName,
                operatorEmail
        );
    }
}
