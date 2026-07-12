package com.eulji.clubon.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 학교 이메일 인증번호 발송 요청입니다.
public record SendEmailVerificationRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 50, message = "이메일은 최대 50자까지 입력할 수 있습니다.")
    String email
) {
}
