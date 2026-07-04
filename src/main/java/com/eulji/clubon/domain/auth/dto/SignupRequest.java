package com.eulji.clubon.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(max = 50, message = "이메일은 최대 50자까지 입력할 수 있습니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s])\\S{8,16}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상 16자 이하여야 합니다."
        )
        String password,

        @NotBlank(message = "이름은 필수입니다.")
        @Pattern(regexp = "^[가-힣A-Za-z0-9]{2,10}$", message = "이름은 2자 이상 10자 이하이며 특수문자를 사용할 수 없습니다.")
        String name,

        @NotBlank(message = "학번은 필수입니다.")
        @Pattern(regexp = "^\\d{10}$", message = "학번은 10자리 숫자여야 합니다.")
        String studentId,

        @NotBlank(message = "소속학과는 필수입니다.")
        String department
) {
}
