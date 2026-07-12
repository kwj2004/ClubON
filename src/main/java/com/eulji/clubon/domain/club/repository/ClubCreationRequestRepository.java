package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubCreationRequest;
import com.eulji.clubon.domain.club.entity.ClubCreationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubCreationRequestRepository extends JpaRepository<ClubCreationRequest, Long> {

    boolean existsByNameAndStatus(String name, ClubCreationRequestStatus status);

    List<ClubCreationRequest> findByRequester_EmailOrderByCreatedAtDesc(String email);

    Optional<ClubCreationRequest> findByIdAndRequester_Email(Long requestId, String email);

    @Query(
        value = """
                select ccr
                from ClubCreationRequest ccr
                join fetch ccr.requester
                left join fetch ccr.createdClub
                where (:status is null or ccr.status = :status)
                order by ccr.createdAt desc, ccr.id desc
                """,
        countQuery = """
                select count(ccr)
                from ClubCreationRequest ccr
                where (:status is null or ccr.status = :status)
                """
    )
    Page<ClubCreationRequest> findAdminRequests(
        @Param("status") ClubCreationRequestStatus status,
        Pageable pageable
    );
    @Query("""
        select ccr
        from ClubCreationRequest ccr
        join fetch ccr.requester
        left join fetch ccr.createdClub
        where ccr.id = :requestId
        """)
    Optional<ClubCreationRequest> findAdminRequestDetail(@Param("requestId") Long requestId);
}
