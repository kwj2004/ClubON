package com.eulji.clubon.domain.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RecordImageUploadRequest(
    @NotBlank(message = "파일 이름은 필수입니다.")
    @Size(max = 255, message = "파일 이름은 255자 이하여야 합니다.")
    String fileName,

    @NotBlank(message = "Content-Type은 필수입니다.")
    String contentType,

    @NotNull(message = "파일 크기는 필수입니다.")
    @Positive(message = "파일 크기는 0보다 커야 합니다.")
    Long fileSize
) {
}
