package com.eulji.clubon.domain.auth.dto;

public record TokenInfo(
        String grantType,
        String accessToken,
        long expiresIn,
        String refreshToken,
        Long refreshExpiresIn
) {

    public static TokenInfo bearer(String accessToken, long expiresIn) {
        return new TokenInfo("Bearer", accessToken, expiresIn, null, null);
    }

    public TokenInfo withRefreshToken(String refreshToken, long refreshExpiresIn) {
        return new TokenInfo(grantType, accessToken, expiresIn, refreshToken, refreshExpiresIn);
    }
}
