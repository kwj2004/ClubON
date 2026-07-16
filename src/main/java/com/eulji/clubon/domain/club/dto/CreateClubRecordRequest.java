package com.eulji.clubon.domain.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateClubRecordRequest(
    @NotBlank(message = "활동 기록 제목을 입력해주세요.")
    @Size(min = 2, max = 50, message = "활동 기록 제목은 2자 이상 50자 이하로 입력해주세요.")
    String title,

    @NotBlank(message = "활동 기록 내용을 입력해주세요.")
    String content,

    @Size(max = 5, message = "이미지는 최대 5개까지 첨부할 수 있습니다.")
    List<@Size(max = 500, message = "이미지 객체 키는 500자 이하여야 합니다.") String> imageUrls
) {
}
