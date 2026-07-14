package com.eulji.clubon.domain.auth.repository;

import com.eulji.clubon.domain.auth.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findTopByMemberEmailOrderByCreatedAtDesc(String email);
    Optional<PasswordReset> findByResetTokenHash(String resetTokenHash);
}
