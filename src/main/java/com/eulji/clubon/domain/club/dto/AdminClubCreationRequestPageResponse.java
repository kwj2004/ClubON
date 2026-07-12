package com.eulji.clubon.domain.club.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record AdminClubCreationRequestPageResponse(
    List<AdminClubCreationRequestListResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {

    public static AdminClubCreationRequestPageResponse from(Page<AdminClubCreationRequestListResponse> page) {
        return new AdminClubCreationRequestPageResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
