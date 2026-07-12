package com.eulji.clubon.domain.auth.dto;

// 학교 이메일 인증번호 확인 결과입니다.
public record ConfirmEmailVerificationResponse(
    String email,
    boolean verified
) {
}
