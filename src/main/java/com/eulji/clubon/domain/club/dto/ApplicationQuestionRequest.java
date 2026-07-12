package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ApplicationQuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ApplicationQuestionRequest(

    @NotBlank(message = "질문명을 입력해주세요.")
    @Size(max = 100, message = "질문명은 100자 이하로 입력해주세요.")
    String label,

    @NotNull(message = "질문 유형을 선택해주세요.")
    ApplicationQuestionType type,

    boolean required,

    @Min(value = 0, message = "질문 순서는 0 이상이어야 합니다.")
    int sortOrder,

    List<
        @NotBlank(message = "선택지는 빈 값일 수 없습니다.")
        @Size(max = 100, message = "선택지는 100자 이하로 입력해주세요.")
            String
        > options
) {
}

// 질문 요청 DTO 생성
