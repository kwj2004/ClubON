package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import jakarta.validation.constraints.NotNull;

public record UpdateClubMemberRoleRequest(
    @NotNull(message = "변경할 멤버 역할을 선택해주세요.")
    ClubMemberRole role
) {
}
