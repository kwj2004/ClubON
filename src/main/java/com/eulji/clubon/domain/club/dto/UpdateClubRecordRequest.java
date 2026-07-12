package com.eulji.clubon.domain.club.dto;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record UpdateClubRecordRequest(
    @Size(min = 2, max = 50, message = "활동 기록 제목은 2자 이상 50자 이하로 입력해주세요.")
    String title,

    String content,

    @Size(max = 5, message = "이미지는 최대 5개까지 첨부할 수 있습니다.")
    List<@URL(message = "이미지 URL 형식이 올바르지 않습니다.") String> imageUrls
) {
}
