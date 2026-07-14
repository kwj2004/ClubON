package com.eulji.clubon.domain.auth.repository;

import com.eulji.clubon.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import com.eulji.clubon.domain.member.entity.Member;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    List<RefreshToken> findAllByMemberAndRevokedFalse(Member member);
}
