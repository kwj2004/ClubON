package com.eulji.clubon.domain.auth.repository;

import com.eulji.clubon.domain.auth.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 학교 이메일 인증번호 저장/조회용 Repository입니다.
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findTopByEmailOrderByCreatedAtDesc(String email);

    boolean existsByEmailAndVerifiedTrue(String email);
}
