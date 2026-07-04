package com.eulji.clubon.domain.auth.controller;

import com.eulji.clubon.domain.auth.dto.LoginRequest;
import com.eulji.clubon.domain.auth.dto.LoginResponse;
import com.eulji.clubon.domain.auth.dto.SignupRequest;
import com.eulji.clubon.domain.auth.dto.SignupResponse;
import com.eulji.clubon.domain.auth.service.AuthService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "회원가입이 성공적으로 완료되었습니다.",
                        response
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "로그인에 성공했습니다.",
                response
        ));
    }
}
