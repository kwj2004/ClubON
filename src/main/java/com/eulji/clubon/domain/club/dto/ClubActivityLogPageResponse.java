package com.eulji.clubon.domain.club.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record ClubActivityLogPageResponse(
    List<ClubActivityLogResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {

    public static ClubActivityLogPageResponse from(Page<ClubActivityLogResponse> page) {
        return new ClubActivityLogPageResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
