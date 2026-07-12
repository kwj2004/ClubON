package com.eulji.clubon.domain.inquiry.dto;

import com.eulji.clubon.domain.inquiry.entity.InquiryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateInquiryRequest(

    @NotNull(message = "문의 유형을 선택해주세요.")
    InquiryType type,

    @NotBlank(message = "문의 제목을 입력해주세요.")
    @Size(max = 100, message = "문의 제목은 100자 이하로 입력해주세요.")
    String title,

    @NotBlank(message = "문의 내용을 입력해주세요.")
    @Size(max = 3000, message = "문의 내용은 3000자 이하로 입력해주세요.")
    String content,

    @URL(message = "첨부파일 URL 형식이 올바르지 않습니다.")
    @Size(max = 500, message = "첨부파일 URL은 500자 이하이어야 합니다.")
    String attachmentUrl
) {
}
//문의 등록 요청 DTO
