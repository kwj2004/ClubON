package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubRecordRepository extends JpaRepository<ClubRecord, Long> {

    List<ClubRecord> findByClub_IdOrderByCreatedAtDesc(Long clubId);

    @Query("""
        select cr
        from ClubRecord cr
        join fetch cr.club
        where cr.id = :recordId
          and cr.club.id = :clubId
        """)
    Optional<ClubRecord> findByIdAndClubId(
        @Param("recordId") Long recordId,
        @Param("clubId") Long clubId
    );
}
