package com.eulji.clubon.domain.auth.dto;
public record PasswordResetVerifyResponse(String resetToken, long expiresIn) {}
