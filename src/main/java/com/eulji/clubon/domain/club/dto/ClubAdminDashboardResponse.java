package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import com.eulji.clubon.domain.club.entity.ClubType;

public record ClubAdminDashboardResponse(
    Long clubId,
    String name,
    ClubType type,
    ClubStatus status,
    String imageUrl,
    String recruitPeriod,
    String recruitCondition,
    long totalApplications,
    long pendingApplications,
    long approvedApplications,
    long rejectedApplications,
    long canceledApplications,
    long interviewScheduledApplications,
    long memberCount,
    long postCount
) {

    public static ClubAdminDashboardResponse of(
        Club club,
        long totalApplications,
        long pendingApplications,
        long approvedApplications,
        long rejectedApplications,
        long canceledApplications,
        long memberCount,
        long postCount
    ) {
        return new ClubAdminDashboardResponse(
            club.getId(),
            club.getName(),
            club.getType(),
            club.getStatus(),
            club.getImageUrl(),
            club.getRecruitPeriod(),
            club.getRecruitCondition(),
            totalApplications,
            pendingApplications,
            approvedApplications,
            rejectedApplications,
            canceledApplications,
            0,
            memberCount,
            postCount
        );
    }
}
