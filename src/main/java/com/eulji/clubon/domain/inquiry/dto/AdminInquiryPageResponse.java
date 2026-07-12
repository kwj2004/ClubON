package com.eulji.clubon.domain.inquiry.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record AdminInquiryPageResponse(
    List<AdminInquiryListResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    public static AdminInquiryPageResponse from(Page<AdminInquiryListResponse> page) {
        return new AdminInquiryPageResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}
