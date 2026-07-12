package com.eulji.clubon.domain.club.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record MyClubReviewPageResponse(
    List<MyClubReviewListResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {

    public static MyClubReviewPageResponse from(Page<MyClubReviewListResponse> page) {
        return new MyClubReviewPageResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
