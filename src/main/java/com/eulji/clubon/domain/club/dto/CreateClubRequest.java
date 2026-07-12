package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubCategory;
import com.eulji.clubon.domain.club.entity.ClubType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.util.List;

public record CreateClubRequest(
        @NotBlank(message = "동아리 이름은 필수입니다.")
        @Size(min = 2, max = 20, message = "동아리 이름은 2자 이상 20자 이하여야 합니다.")
        String name,

        @NotNull(message = "동아리 구분은 필수입니다.")
        ClubType type,

        ClubCategory category,

        @NotBlank(message = "한 줄 소개는 필수입니다.")
        @Size(max = 100, message = "한 줄 소개는 최대 100자까지 입력할 수 있습니다.")
        String shortDescription,

        @NotBlank(message = "상세 소개글은 필수입니다.")
        String fullDescription,

        @Valid
        @Size(max = 20, message = "지원서 질문은 최대 20개까지 등록할 수 있습니다.")
            List<ApplicationQuestionRequest> applicationQuestions
) {
}
