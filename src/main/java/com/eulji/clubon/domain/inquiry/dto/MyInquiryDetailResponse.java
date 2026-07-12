package com.eulji.clubon.domain.inquiry.dto;

import com.eulji.clubon.domain.inquiry.entity.Inquiry;
import com.eulji.clubon.domain.inquiry.entity.InquiryStatus;
import com.eulji.clubon.domain.inquiry.entity.InquiryType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MyInquiryDetailResponse(
    Long inquiryId,
    InquiryType type,
    String title,
    String content,
    String attachmentUrl,
    InquiryStatus status,
    String answer,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime answeredAt
) {
    public static MyInquiryDetailResponse from(Inquiry inquiry) {
        return new MyInquiryDetailResponse(
            inquiry.getId(),
            inquiry.getType(),
            inquiry.getTitle(),
            inquiry.getContent(),
            inquiry.getAttachmentUrl(),
            inquiry.getStatus(),
            inquiry.getAnswer(),
            inquiry.getCreatedAt(),
            inquiry.getAnsweredAt()
        );
    }
}
