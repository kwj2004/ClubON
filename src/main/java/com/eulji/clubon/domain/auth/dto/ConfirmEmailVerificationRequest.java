package com.eulji.clubon.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// 사용자가 입력한 학교 이메일 인증번호 확인 요청입니다.
public record ConfirmEmailVerificationRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 50, message = "이메일은 최대 50자까지 입력할 수 있습니다.")
    String email,

    @NotBlank(message = "인증번호를 입력해주세요.")
    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자입니다.")
    String code
) {
}
