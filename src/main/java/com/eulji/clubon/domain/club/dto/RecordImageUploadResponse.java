package com.eulji.clubon.domain.club.dto;

public record RecordImageUploadResponse(
    String uploadUrl,
    String objectKey,
    long expiresIn
) {
}
