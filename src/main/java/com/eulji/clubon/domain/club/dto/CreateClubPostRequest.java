package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubPostCategory;
import com.eulji.clubon.domain.club.entity.ClubPostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record CreateClubPostRequest(
    @NotNull(message = "게시글 분류를 선택해주세요.")
    ClubPostCategory category,

    ClubPostStatus status,

    @NotBlank(message = "게시글 제목을 입력해주세요.")
    @Size(max = 100, message = "게시글 제목은 100자 이하로 입력해주세요.")
    String title,

    @NotBlank(message = "게시글 내용을 입력해주세요.")
    String content,

    @Size(max = 5, message = "첨부파일은 최대 5개까지 등록할 수 있습니다.")
    List<@URL(message = "첨부파일 URL 형식이 올바르지 않습니다.") String> attachmentUrls
) {
}
