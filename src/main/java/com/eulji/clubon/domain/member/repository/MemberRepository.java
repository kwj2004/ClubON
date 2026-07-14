package com.eulji.clubon.domain.member.repository;

import com.eulji.clubon.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNameAndStudentId(String name, String studentId);

    boolean existsByEmail(String email);

    boolean existsByStudentId(String studentId);
}
