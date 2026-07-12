package com.eulji.clubon.domain.club.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record ClubReviewPageResponse(
    List<ClubReviewListResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {

    public static ClubReviewPageResponse from(Page<ClubReviewListResponse> page) {
        return new ClubReviewPageResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
