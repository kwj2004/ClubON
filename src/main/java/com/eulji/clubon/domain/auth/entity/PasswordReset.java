package com.eulji.clubon.domain.auth.entity;

import com.eulji.clubon.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "password_resets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordReset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member member;
    @Column(nullable = false, length = 6)
    private String code;
    @Column(length = 64, unique = true)
    private String resetTokenHash;
    @Column(nullable = false)
    private LocalDateTime codeExpiresAt;
    private LocalDateTime tokenExpiresAt;
    @Column(nullable = false)
    private boolean used;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public PasswordReset(Member member, String code, LocalDateTime codeExpiresAt) {
        this.member = member; this.code = code; this.codeExpiresAt = codeExpiresAt;
        this.createdAt = LocalDateTime.now();
    }
    public boolean canVerify(String input, LocalDateTime now) { return !used && code.equals(input) && codeExpiresAt.isAfter(now); }
    public void issueToken(String hash, LocalDateTime expiresAt) { this.resetTokenHash = hash; this.tokenExpiresAt = expiresAt; }
    public boolean canReset(LocalDateTime now) { return !used && resetTokenHash != null && tokenExpiresAt.isAfter(now); }
    public void use() { this.used = true; }
}
