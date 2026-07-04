package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubCreationRequest;
import com.eulji.clubon.domain.club.entity.ClubCreationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubCreationRequestRepository extends JpaRepository<ClubCreationRequest, Long> {

    boolean existsByNameAndStatus(String name, ClubCreationRequestStatus status);

    List<ClubCreationRequest> findByRequester_EmailOrderByCreatedAtDesc(String email);

    Optional<ClubCreationRequest> findByIdAndRequester_Email(Long requestId, String email);
}
