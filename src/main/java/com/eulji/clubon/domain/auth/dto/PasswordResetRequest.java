package com.eulji.clubon.domain.auth.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record PasswordResetRequest(
    @NotBlank String resetToken,
    @NotBlank @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다.") String newPassword
) {}
