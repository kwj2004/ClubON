package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubReviewRepository extends JpaRepository<ClubReview, Long> {

    boolean existsByClub_IdAndMember_Email(Long clubId, String email);

    @Query(value = """
            select cr
            from ClubReview cr
            join fetch cr.member
            join fetch cr.club
            where cr.club.id = :clubId
            order by cr.createdAt desc
            """,
            countQuery = """
            select count(cr)
            from ClubReview cr
            where cr.club.id = :clubId
            """)
    Page<ClubReview> findByClubId(
        @Param("clubId") Long clubId,
        Pageable pageable
    );

    @Query(value = """
            select cr
            from ClubReview cr
            join fetch cr.member
            join fetch cr.club
            where cr.member.email = :email
            order by cr.createdAt desc
            """,
            countQuery = """
            select count(cr)
            from ClubReview cr
            where cr.member.email = :email
            """)
    Page<ClubReview> findByMemberEmail(
        @Param("email") String email,
        Pageable pageable
    );

    @Query("""
            select cr
            from ClubReview cr
            join fetch cr.member
            join fetch cr.club
            where cr.id = :reviewId
              and cr.club.id = :clubId
              and cr.member.email = :email
            """)
    Optional<ClubReview> findOwnedReview(
        @Param("clubId") Long clubId,
        @Param("reviewId") Long reviewId,
        @Param("email") String email
    );
}
