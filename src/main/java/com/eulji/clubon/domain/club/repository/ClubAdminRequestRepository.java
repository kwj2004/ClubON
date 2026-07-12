package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubAdminRequest;
import com.eulji.clubon.domain.club.entity.ClubAdminRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

// 동아리 운영자 신청 저장/조회용 Repository입니다.
public interface ClubAdminRequestRepository extends JpaRepository<ClubAdminRequest, Long> {

    boolean existsByMember_EmailAndClub_IdAndStatus(
        String email,
        Long clubId,
        ClubAdminRequestStatus status
    );
}
