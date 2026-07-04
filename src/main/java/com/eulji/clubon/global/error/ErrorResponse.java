package com.eulji.clubon.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String error,
        String message,
        List<FieldErrorResponse> errors
) {

    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, null);
    }

    public static ErrorResponse withErrors(
            int status,
            String error,
            String message,
            List<FieldErrorResponse> errors
    ) {
        return new ErrorResponse(status, error, message, errors);
    }
}
