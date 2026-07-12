package com.eulji.clubon.domain.inquiry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnswerInquiryRequest(
    @NotBlank(message = "문의 답변을 입력해주세요.")
    @Size(max = 3000, message = "문의 답변은 3000자 이하로 입력해주세요.")
    String answer
) {
}
