package com.eulji.clubon.domain.auth.service;

import com.eulji.clubon.domain.auth.dto.ConfirmEmailVerificationRequest;
import com.eulji.clubon.domain.auth.dto.ConfirmEmailVerificationResponse;
import com.eulji.clubon.domain.auth.dto.SendEmailVerificationRequest;
import com.eulji.clubon.domain.auth.dto.SendEmailVerificationResponse;
import com.eulji.clubon.domain.auth.entity.EmailVerification;
import com.eulji.clubon.domain.auth.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

// 학교 이메일 인증번호 발급, 메일 발송, 확인, 가입 전 인증 여부 검증을 담당합니다.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailVerificationService {

    private static final int CODE_BOUND = 1_000_000;
    private static final int CODE_EXPIRES_MINUTES = 5;
    private static final String SCHOOL_EMAIL_DOMAIN = "@g.eulji.ac.kr";

    private final EmailVerificationRepository emailVerificationRepository;
    private final MailService mailService;
    private final SecureRandom secureRandom = new SecureRandom();

    // 6자리 인증번호를 생성해 저장한 뒤 실제 학교 이메일로 발송합니다.
    @Transactional
    public SendEmailVerificationResponse sendCode(SendEmailVerificationRequest request) {
        validateSchoolEmailDomain(request.email());

        String code = String.format("%06d", secureRandom.nextInt(CODE_BOUND));
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(CODE_EXPIRES_MINUTES);

        EmailVerification verification = emailVerificationRepository.save(EmailVerification.builder()
            .email(request.email())
            .code(code)
            .expiresAt(expiresAt)
            .build());

        mailService.sendVerificationCode(verification.getEmail(), code);

        return new SendEmailVerificationResponse(
            verification.getEmail(),
            verification.getExpiresAt()
        );
    }

    // 가장 최근에 발급된 인증번호를 기준으로 만료 여부와 일치 여부를 확인합니다.
    @Transactional
    public ConfirmEmailVerificationResponse confirmCode(ConfirmEmailVerificationRequest request) {
        validateSchoolEmailDomain(request.email());

        EmailVerification verification = emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(request.email())
            .orElseThrow(() -> new IllegalArgumentException("인증번호 발송 내역이 없습니다."));

        if (verification.isExpired(LocalDateTime.now())) {
            throw new IllegalArgumentException("인증번호가 만료되었습니다.");
        }

        if (!verification.matches(request.code())) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        verification.verify();

        return new ConfirmEmailVerificationResponse(
            verification.getEmail(),
            true
        );
    }

    // 회원가입은 인증 완료된 을지대 학교 이메일만 허용합니다.
    public void validateVerifiedEmail(String email) {
        validateSchoolEmailDomain(email);

        if (!emailVerificationRepository.existsByEmailAndVerifiedTrue(email)) {
            throw new IllegalArgumentException("학교 이메일 인증을 완료해주세요.");
        }
    }

    private void validateSchoolEmailDomain(String email) {
        if (email == null || !email.toLowerCase().endsWith(SCHOOL_EMAIL_DOMAIN)) {
            throw new IllegalArgumentException("학교 이메일은 @g.eulji.ac.kr 도메인만 사용할 수 있습니다.");
        }
    }
}
