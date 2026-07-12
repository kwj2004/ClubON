package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.AdminClubStatisticsResponse;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import com.eulji.clubon.domain.club.entity.ClubType;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eulji.clubon.domain.club.dto.AdminClubListResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminClubService {

    private final ClubRepository clubRepository;

    public AdminClubStatisticsResponse getClubStatistics() {
        return new AdminClubStatisticsResponse(
            clubRepository.count(),
            clubRepository.countByType(ClubType.CENTRAL),
            clubRepository.countByType(ClubType.GENERAL),
            clubRepository.countByStatus(ClubStatus.OPEN)
        );
    }
    public List<AdminClubListResponse> getAdminClubs(ClubType type, Boolean isActive) {
        ClubStatus status = null;

        if (isActive != null) {
            status = isActive ? ClubStatus.OPEN : ClubStatus.CLOSED;
        }

        return clubRepository.findAdminClubs(type, status)
            .stream()
            .map(AdminClubListResponse::from)
            .toList();
    }
}
