package com.eulji.clubon.domain.club.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ApplicationAnswerRequest(

    @NotNull(message = "질문 ID는 필수입니다.")
    Long questionId,

    @NotNull(message = "답변 목록은 필수입니다.")
    @Size(max = 20, message = "답변 값은 최대 20개까지 선택할 수 있습니다.")
    List<
        @Size(max = 2000, message = "답변은 2000자 이하로 입력해주세요.")
            String
        > values
) {
}
