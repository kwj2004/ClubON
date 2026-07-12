package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateClubApplicationStatusRequest(
    @NotNull(message = "처리 상태는 필수입니다.")
    ClubApplicationStatus status
) {
}
