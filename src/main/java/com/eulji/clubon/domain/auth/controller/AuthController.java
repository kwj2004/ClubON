package com.eulji.clubon.domain.auth.controller;

import com.eulji.clubon.domain.auth.dto.ConfirmEmailVerificationRequest;
import com.eulji.clubon.domain.auth.dto.ConfirmEmailVerificationResponse;
import com.eulji.clubon.domain.auth.dto.LoginRequest;
import com.eulji.clubon.domain.auth.dto.LoginResponse;
import com.eulji.clubon.domain.auth.dto.SendEmailVerificationRequest;
import com.eulji.clubon.domain.auth.dto.SendEmailVerificationResponse;
import com.eulji.clubon.domain.auth.dto.SignupRequest;
import com.eulji.clubon.domain.auth.dto.SignupResponse;
import com.eulji.clubon.domain.auth.service.AuthService;
import com.eulji.clubon.domain.auth.service.EmailVerificationService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 회원가입/로그인과 학교 이메일 인증 API를 담당하는 컨트롤러입니다.
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    // 학교 이메일로 인증번호를 발급합니다. 현재는 개발 편의를 위해 응답에 devCode를 함께 내려줍니다.
    @PostMapping("/email-verifications/send")
    public ResponseEntity<ApiResponse<SendEmailVerificationResponse>> sendEmailVerification(
            @Valid @RequestBody SendEmailVerificationRequest request
    ) {
        SendEmailVerificationResponse response = emailVerificationService.sendCode(request);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "인증번호가 발송되었습니다.",
                response
        ));
    }

    // 사용자가 입력한 인증번호를 확인하고 해당 이메일을 인증 완료 상태로 변경합니다.
    @PostMapping("/email-verifications/confirm")
    public ResponseEntity<ApiResponse<ConfirmEmailVerificationResponse>> confirmEmailVerification(
            @Valid @RequestBody ConfirmEmailVerificationRequest request
    ) {
        ConfirmEmailVerificationResponse response = emailVerificationService.confirmCode(request);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "학교 이메일 인증이 완료되었습니다.",
                response
        ));
    }

    // 일반 회원가입과 동아리 운영자 신청 회원가입을 함께 처리합니다.
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

    // 이메일/비밀번호를 검증하고 JWT access token을 발급합니다.
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
