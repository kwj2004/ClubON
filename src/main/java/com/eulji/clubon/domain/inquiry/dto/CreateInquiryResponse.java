package com.eulji.clubon.domain.inquiry.dto;

import com.eulji.clubon.domain.inquiry.entity.Inquiry;
import com.eulji.clubon.domain.inquiry.entity.InquiryStatus;

public record CreateInquiryResponse(
    Long inquiryId,
    InquiryStatus status
) {
    public static CreateInquiryResponse from(Inquiry inquiry) {
        return new CreateInquiryResponse(
            inquiry.getId(),
            inquiry.getStatus()
        );
    }
}
//문의 등록 응답 DTO
