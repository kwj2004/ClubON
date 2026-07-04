package com.eulji.clubon.domain.member.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateMyProfileRequest(
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s])\\S{8,16}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상 16자 이하여야 합니다."
        )
        String password,

        @Pattern(regexp = "^[가-힣A-Za-z0-9]{2,10}$", message = "이름은 2자 이상 10자 이하이며 특수문자를 사용할 수 없습니다.")
        String name,

        @Pattern(regexp = "^\\S.*$", message = "소속학과는 공백일 수 없습니다.")
        @Size(max = 50, message = "소속학과는 최대 50자까지 입력할 수 있습니다.")
        String department
) {
}
