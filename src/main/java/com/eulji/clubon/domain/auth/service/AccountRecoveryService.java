package com.eulji.clubon.domain.auth.service;

import com.eulji.clubon.domain.auth.dto.*;
import com.eulji.clubon.domain.auth.entity.PasswordReset;
import com.eulji.clubon.domain.auth.repository.PasswordResetRepository;
import com.eulji.clubon.domain.auth.repository.RefreshTokenRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountRecoveryService {
    private final MemberRepository memberRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom random = new SecureRandom();
    @Value("${auth.password-reset.code-expiration}") private long codeExpiration;
    @Value("${auth.password-reset.token-expiration}") private long tokenExpiration;

    public FindLoginIdResponse findLoginId(FindLoginIdRequest request) {
        Member member = memberRepository.findByNameAndStudentId(request.name(), request.studentId())
            .orElseThrow(() -> new IllegalArgumentException("일치하는 회원 정보가 없습니다."));
        return new FindLoginIdResponse(mask(member.getEmail()));
    }

    @Transactional
    public void sendResetCode(PasswordResetSendRequest request) {
        Member member = memberRepository.findByEmail(request.email()).orElse(null);
        if (member == null) return;
        String code = String.format("%06d", random.nextInt(1_000_000));
        passwordResetRepository.save(PasswordReset.builder().member(member).code(code)
            .codeExpiresAt(LocalDateTime.now().plusNanos(codeExpiration * 1_000_000)).build());
        mailService.sendPasswordResetCode(member.getEmail(), code);
    }

    @Transactional
    public PasswordResetVerifyResponse verifyResetCode(PasswordResetVerifyRequest request) {
        PasswordReset reset = passwordResetRepository.findTopByMemberEmailOrderByCreatedAtDesc(request.email())
            .orElseThrow(() -> new IllegalArgumentException("인증번호 발송 내역이 없습니다."));
        if (!reset.canVerify(request.code(), LocalDateTime.now()))
            throw new IllegalArgumentException("인증번호가 일치하지 않거나 만료되었습니다.");
        String raw = randomToken();
        reset.issueToken(hash(raw), LocalDateTime.now().plusNanos(tokenExpiration * 1_000_000));
        return new PasswordResetVerifyResponse(raw, tokenExpiration / 1000);
    }

    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        PasswordReset reset = passwordResetRepository.findByResetTokenHash(hash(request.resetToken()))
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 비밀번호 재설정 토큰입니다."));
        if (!reset.canReset(LocalDateTime.now())) throw new IllegalArgumentException("비밀번호 재설정 토큰이 만료되었거나 이미 사용되었습니다.");
        reset.getMember().changePassword(passwordEncoder.encode(request.newPassword()));
        refreshTokenRepository.findAllByMemberAndRevokedFalse(reset.getMember()).forEach(token -> token.revoke());
        reset.use();
    }

    private String mask(String email) {
        int at = email.indexOf('@'); String id = email.substring(0, at);
        int visible = Math.min(2, id.length());
        return id.substring(0, visible) + "*".repeat(Math.max(1, id.length() - visible)) + email.substring(at);
    }
    private String randomToken() { byte[] b = new byte[32]; random.nextBytes(b); return Base64.getUrlEncoder().withoutPadding().encodeToString(b); }
    private String hash(String value) {
        try { return java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }
}
