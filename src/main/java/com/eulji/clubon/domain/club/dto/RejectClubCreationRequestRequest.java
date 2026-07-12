package com.eulji.clubon.domain.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RejectClubCreationRequestRequest(
    @NotBlank(message = "거절 사유를 입력해주세요.")
    @Size(max = 500, message = "거절 사유는 500자 이하로 입력해주세요.")
    String rejectedReason
) {
}
