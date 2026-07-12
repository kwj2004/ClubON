package com.eulji.clubon.domain.club.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateClubApplicationRequest(
//빈 질문 양식을 사용하는 동아리도 존재할 가능성 있음.
    @NotNull(message = "지원서 답변 목록은 필수입니다.")
    @Size(max = 20, message = "지원서 답변은 최대 20개까지 작성할 수 있습니다.")
    List<@Valid ApplicationAnswerRequest> answers
) {
}
