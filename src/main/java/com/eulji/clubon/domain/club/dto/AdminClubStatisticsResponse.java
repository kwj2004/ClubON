package com.eulji.clubon.domain.club.dto;

public record AdminClubStatisticsResponse(
    long totalClubs,
    long centralClubs,
    long generalClubs,
    long recruitingClubs
) {
}
