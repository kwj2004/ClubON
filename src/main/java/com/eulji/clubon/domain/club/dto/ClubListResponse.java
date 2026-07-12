package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.util.RecruitmentStatusResolver;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ClubListResponse(
        Long clubId,
        String name,
        String type,
        String category,
        String status,
        String shortDescription,
        String imageUrl,
        String recruitPeriod,
        String recruitmentStatus,
        String recruitmentStatusLabel,
        @JsonProperty("isRecruiting")
        boolean isRecruiting
) {

    public static ClubListResponse from(Club club) {
        RecruitmentStatusInfo recruitmentStatusInfo = RecruitmentStatusResolver.resolve(
                club.getStatus(),
                club.getRecruitPeriod()
        );

        return new ClubListResponse(
                club.getId(),
                club.getName(),
                club.getType().name(),
                club.getCategory().name(),
                club.getStatus().name(),
                club.getShortDescription(),
                club.getImageUrl(),
                club.getRecruitPeriod(),
                recruitmentStatusInfo.status(),
                recruitmentStatusInfo.label(),
                recruitmentStatusInfo.isRecruiting()
        );
    }
}
