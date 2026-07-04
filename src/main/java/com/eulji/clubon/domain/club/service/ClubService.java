package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubDetailResponse;
import com.eulji.clubon.domain.club.dto.ClubListResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubRequest;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import com.eulji.clubon.domain.club.entity.ClubType;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.global.error.ClubNotFoundException;
import com.eulji.clubon.global.error.InvalidClubUpdateException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMembershipRepository clubMembershipRepository;
    private final ClubBookmarkService clubBookmarkService;

    public List<ClubListResponse> getClubs(ClubType type, ClubStatus status, String keyword) {
        String normalizedKeyword = normalizeKeyword(keyword);

        return clubRepository.searchClubs(type, status, normalizedKeyword)
                .stream()
                .map(ClubListResponse::from)
                .toList();
    }

    public ClubDetailResponse getClubDetail(Long clubId, String email) {
        return clubRepository.findById(clubId)
                .map(club -> ClubDetailResponse.of(club, clubBookmarkService.isBookmarked(clubId, email)))
                .orElseThrow(ClubNotFoundException::new);
    }

    @Transactional
    public void updateClub(Long clubId, UpdateClubRequest request, String email) {
        if (!request.hasAnyValue()) {
            throw new InvalidClubUpdateException("수정할 값이 없습니다.");
        }

        Club club = clubRepository.findById(clubId)
                .orElseThrow(ClubNotFoundException::new);

        if (!clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(clubId, email, ClubMemberRole.ADMIN)) {
            throw new AccessDeniedException("동아리 정보를 수정할 권한이 없습니다.");
        }

        validateRecruitingInfo(club, request);

        club.updateInfo(
                request.status(),
                trimOrNull(request.shortDescription()),
                trimOrNull(request.fullDescription()),
                trimOrNull(request.recruitPeriod()),
                trimOrNull(request.recruitCondition()),
                trimOrNull(request.activityInfo()),
                trimOrNull(request.contactUrl()),
                trimOrNull(request.imageUrl())
        );
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.trim();
    }

    private void validateRecruitingInfo(Club club, UpdateClubRequest request) {
        ClubStatus finalStatus = request.status() == null ? club.getStatus() : request.status();
        String finalRecruitPeriod = request.recruitPeriod() == null
                ? club.getRecruitPeriod()
                : request.recruitPeriod().trim();
        String finalRecruitCondition = request.recruitCondition() == null
                ? club.getRecruitCondition()
                : request.recruitCondition().trim();

        if (finalStatus == ClubStatus.OPEN && isBlank(finalRecruitPeriod)) {
            throw new InvalidClubUpdateException("모집 중일 때는 모집 기간 안내가 필요합니다.");
        }

        if (finalStatus == ClubStatus.OPEN && isBlank(finalRecruitCondition)) {
            throw new InvalidClubUpdateException("모집 중일 때는 지원 조건이 필요합니다.");
        }
    }

    private String trimOrNull(String value) {
        if (value == null) {
            return null;
        }

        return value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
