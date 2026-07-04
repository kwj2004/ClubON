package com.eulji.clubon.domain.auth.dto;

public record TokenInfo(
        String grantType,
        String accessToken,
        long expiresIn
) {

    public static TokenInfo bearer(String accessToken, long expiresIn) {
        return new TokenInfo("Bearer", accessToken, expiresIn);
    }
}
