package com.eulji.clubon.domain.club.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record ClubPostPageResponse(
    List<ClubPostListResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {

    public static ClubPostPageResponse from(Page<ClubPostListResponse> page) {
        return new ClubPostPageResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
