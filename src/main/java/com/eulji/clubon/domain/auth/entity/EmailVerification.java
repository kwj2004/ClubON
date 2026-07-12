package com.eulji.clubon.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "email_verifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 회원가입 전에 학교 이메일 인증번호와 인증 완료 상태를 저장하는 엔티티입니다.
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime verifiedAt;

    @Builder
    public EmailVerification(String email, String code, LocalDateTime expiresAt) {
        this.email = email;
        this.code = code;
        this.expiresAt = expiresAt;
        this.verified = false;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public boolean isExpired(LocalDateTime now) {
        return expiresAt.isBefore(now);
    }

    // 사용자가 입력한 인증번호가 저장된 인증번호와 같은지 확인합니다.
    public boolean matches(String code) {
        return this.code.equals(code);
    }

    // 인증 성공 시 이후 회원가입에서 사용할 수 있도록 인증 완료 상태로 변경합니다.
    public void verify() {
        this.verified = true;
        this.verifiedAt = LocalDateTime.now();
    }
}
