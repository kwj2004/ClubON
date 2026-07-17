package com.eulji.clubon.domain.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RejectClubAdminRequest(
    @NotBlank(message = "거절 사유를 입력해주세요.")
    @Size(max = 500, message = "거절 사유는 최대 500자까지 입력할 수 있습니다.")
    String reason
) {
}
