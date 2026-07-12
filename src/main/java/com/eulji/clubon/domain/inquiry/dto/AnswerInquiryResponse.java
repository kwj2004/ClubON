package com.eulji.clubon.domain.inquiry.dto;

import com.eulji.clubon.domain.inquiry.entity.Inquiry;
import com.eulji.clubon.domain.inquiry.entity.InquiryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AnswerInquiryResponse(
    Long inquiryId,
    InquiryStatus status,
    String answer,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime answeredAt
) {
    public static AnswerInquiryResponse from(Inquiry inquiry) {
        return new AnswerInquiryResponse(
            inquiry.getId(),
            inquiry.getStatus(),
            inquiry.getAnswer(),
            inquiry.getAnsweredAt()
        );
    }
}
