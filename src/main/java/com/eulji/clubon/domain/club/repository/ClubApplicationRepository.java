package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubApplication;
import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.List;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {

    boolean existsByClub_IdAndMember_EmailAndStatus(
        Long clubId,
        String email,
        ClubApplicationStatus status
    );

    Optional<ClubApplication> findByIdAndMember_Email(Long applicationId, String email);

    @Query("""
        select ca
        from ClubApplication ca
        join fetch ca.club
        join ca.member m
        where m.email = :email
        order by ca.appliedAt desc
        """)
    List<ClubApplication> findMyApplications(@Param("email") String email);

    @Query("""
        select ca
        from ClubApplication ca
        join fetch ca.member
        where ca.club.id = :clubId
          and (:status is null or ca.status = :status)
        order by ca.appliedAt desc
        """)
    List<ClubApplication> findClubApplications(
        @Param("clubId") Long clubId,
        @Param("status") ClubApplicationStatus status
    );
    @Query("""
        select ca
        from ClubApplication ca
        join fetch ca.club
        join fetch ca.member
        where ca.id = :applicationId
        """)
    Optional<ClubApplication> findByIdWithClubAndMember(@Param("applicationId") Long applicationId);

    long countByClub_Id(Long clubId);

    long countByClub_IdAndStatus(Long clubId, ClubApplicationStatus status);

    @Query("""
        select count(ca)
        from ClubApplication ca
        where ca.club.id = :clubId
          and ca.status <> com.eulji.clubon.domain.club.entity.ClubApplicationStatus.CANCELED
        """)
    long countActiveApplications(@Param("clubId") Long clubId);
}
