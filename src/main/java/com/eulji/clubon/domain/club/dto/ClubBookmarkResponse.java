package com.eulji.clubon.domain.club.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClubBookmarkResponse(
        Long clubId,
        @JsonProperty("isBookmarked")
        boolean isBookmarked
) {
}
