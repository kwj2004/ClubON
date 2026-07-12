package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubAdminDashboardResponse;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.repository.ClubApplicationRepository;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubPostRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.global.error.ClubNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubAdminDashboardService {

    private final ClubRepository clubRepository;
    private final ClubApplicationRepository clubApplicationRepository;
    private final ClubMembershipRepository clubMembershipRepository;
    private final ClubPostRepository clubPostRepository;

    public ClubAdminDashboardResponse getDashboard(Long clubId, String email) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(ClubNotFoundException::new);

        validateClubAdmin(clubId, email);

        return ClubAdminDashboardResponse.of(
            club,
            clubApplicationRepository.countActiveApplications(clubId),
            clubApplicationRepository.countByClub_IdAndStatus(clubId, ClubApplicationStatus.PENDING),
            clubApplicationRepository.countByClub_IdAndStatus(clubId, ClubApplicationStatus.APPROVED),
            clubApplicationRepository.countByClub_IdAndStatus(clubId, ClubApplicationStatus.REJECTED),
            clubApplicationRepository.countByClub_IdAndStatus(clubId, ClubApplicationStatus.CANCELED),
            clubMembershipRepository.countByClub_Id(clubId),
            clubPostRepository.countByClub_Id(clubId)
        );
    }

    private void validateClubAdmin(Long clubId, String email) {
        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("해당 동아리 운영자만 대시보드를 조회할 수 있습니다.");
        }
    }
}
