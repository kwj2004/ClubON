package com.eulji.clubon.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 회원가입 과정에서 운영자 신청을 할 때 필요한 추가 정보입니다.
public record ClubAdminSignupRequest(
    @NotNull(message = "운영자로 신청할 동아리를 선택해주세요.")
    Long clubId,

    @NotBlank(message = "동아리 내 역할을 입력해주세요.")
    @Size(max = 30, message = "동아리 내 역할은 최대 30자까지 입력할 수 있습니다.")
    String position
) {
}
