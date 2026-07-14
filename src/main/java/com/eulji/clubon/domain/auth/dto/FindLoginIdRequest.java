package com.eulji.clubon.domain.auth.dto;
import jakarta.validation.constraints.NotBlank;
public record FindLoginIdRequest(
    @NotBlank(message = "이름은 필수입니다.") String name,
    @NotBlank(message = "학번은 필수입니다.") String studentId
) {}
