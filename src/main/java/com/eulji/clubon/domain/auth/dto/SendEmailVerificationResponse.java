package com.eulji.clubon.domain.auth.dto;

import java.time.LocalDateTime;

// 학교 이메일 인증번호 발송 결과입니다. 실제 인증번호는 응답에 포함하지 않고 메일로만 발송합니다.
public record SendEmailVerificationResponse(
    String email,
    LocalDateTime expiresAt
) {
}
