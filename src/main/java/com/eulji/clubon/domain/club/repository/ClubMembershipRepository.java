package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubMembership;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubMembershipRepository extends JpaRepository<ClubMembership, Long> {

    @Query("""
            select cm
            from ClubMembership cm
            join fetch cm.club
            join cm.member m
            where m.email = :email
            order by cm.joinedAt desc
            """)
    List<ClubMembership> findMyClubMemberships(@Param("email") String email);

    boolean existsByClub_IdAndMember_EmailAndRole(Long clubId, String email, ClubMemberRole role);
}
