package com.eulji.clubon.domain.club.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record MyClubPostPageResponse(
    List<MyClubPostListResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {

    public static MyClubPostPageResponse from(Page<MyClubPostListResponse> page) {
        return new MyClubPostPageResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
