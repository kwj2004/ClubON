package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubActivityLogRepository extends JpaRepository<ClubActivityLog, Long> {

    @Query(
        value = """
                select log
                from ClubActivityLog log
                left join fetch log.actor
                where log.club.id = :clubId
                order by log.createdAt desc
                """,
        countQuery = """
                select count(log)
                from ClubActivityLog log
                where log.club.id = :clubId
                """
    )
    Page<ClubActivityLog> findByClubId(
        @Param("clubId") Long clubId,
        Pageable pageable
    );
}
