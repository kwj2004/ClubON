package com.eulji.clubon.domain.inquiry.dto;

import com.eulji.clubon.domain.inquiry.entity.Inquiry;
import com.eulji.clubon.domain.inquiry.entity.InquiryStatus;
import com.eulji.clubon.domain.inquiry.entity.InquiryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AdminInquiryListResponse(
    Long inquiryId,
    Long memberId,
    String memberName,
    String memberEmail,
    InquiryType type,
    String title,
    InquiryStatus status,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt
) {
    public static AdminInquiryListResponse from(Inquiry inquiry) {
        return new AdminInquiryListResponse(
            inquiry.getId(),
            inquiry.getMember().getId(),
            inquiry.getMember().getName(),
            inquiry.getMember().getEmail(),
            inquiry.getType(),
            inquiry.getTitle(),
            inquiry.getStatus(),
            inquiry.getCreatedAt()
        );
    }
}
