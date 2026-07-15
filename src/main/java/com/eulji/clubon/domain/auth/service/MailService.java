package com.eulji.clubon.domain.auth.service;

import com.eulji.clubon.global.error.MailSendFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

// 실제 이메일 발송을 담당하는 서비스입니다.
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${clubon.mail.from:}")
    private String from;

    // 학교 이메일 인증번호를 사용자 메일함으로 발송합니다.
    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (StringUtils.hasText(from)) {
            message.setFrom(from);
        }
        message.setTo(to);
        message.setSubject("[동아리 ON] 학교 이메일 인증번호");
        message.setText("""
            안녕하세요. 동아리 ON입니다.

            학교 이메일 인증번호는 [%s] 입니다.
            인증번호는 5분 동안 유효합니다.
            """.formatted(code));

        send(message);
    }

    public void sendPasswordResetCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (StringUtils.hasText(from)) message.setFrom(from);
        message.setTo(to);
        message.setSubject("[동아리 ON] 비밀번호 재설정 인증번호");
        message.setText("비밀번호 재설정 인증번호는 [%s] 입니다. 인증번호는 5분 동안 유효합니다.".formatted(code));
        send(message);
    }

    private void send(SimpleMailMessage message) {
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new MailSendFailedException(e);
        }
    }
}
