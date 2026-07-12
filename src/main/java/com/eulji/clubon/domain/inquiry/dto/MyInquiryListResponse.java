package com.eulji.clubon.domain.inquiry.dto;

import com.eulji.clubon.domain.inquiry.entity.Inquiry;
import com.eulji.clubon.domain.inquiry.entity.InquiryStatus;
import com.eulji.clubon.domain.inquiry.entity.InquiryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MyInquiryListResponse(
    Long inquiryId,
    InquiryType type,
    String title,
    InquiryStatus status,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt
) {
    public static MyInquiryListResponse from(Inquiry inquiry) {
        return new MyInquiryListResponse(
            inquiry.getId(),
            inquiry.getType(),
            inquiry.getTitle(),
            inquiry.getStatus(),
            inquiry.getCreatedAt()
        );
    }
}
