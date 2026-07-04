package com.eulji.clubon.global.error;

public record FieldErrorResponse(
        String field,
        String reason
) {
}
