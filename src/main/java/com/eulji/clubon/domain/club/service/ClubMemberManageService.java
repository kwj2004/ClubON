package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubMemberResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubMemberRoleRequest;
import com.eulji.clubon.domain.club.entity.ClubActivityLogType;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubMembership;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.global.error.ClubMembershipNotFoundException;
import com.eulji.clubon.global.error.ClubNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberManageService {

    private final ClubRepository clubRepository;
    private final ClubMembershipRepository clubMembershipRepository;
    private final ClubActivityLogService clubActivityLogService;

    public List<ClubMemberResponse> getMembers(Long clubId, String email, String keyword) {
        validateClubExists(clubId);
        validateClubAdmin(clubId, email);

        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;

        return clubMembershipRepository.findClubMembers(clubId, normalizedKeyword)
            .stream()
            .map(ClubMemberResponse::from)
            .toList();
    }

    @Transactional
    public void updateMemberRole(
        Long clubId,
        Long membershipId,
        String email,
        UpdateClubMemberRoleRequest request
    ) {
        validateClubExists(clubId);
        validateClubAdmin(clubId, email);

        ClubMembership membership = clubMembershipRepository.findByIdAndClubId(clubId, membershipId)
            .orElseThrow(ClubMembershipNotFoundException::new);

        if (membership.getRole() == ClubMemberRole.ADMIN && request.role() == ClubMemberRole.MEMBER) {
            validateNotLastAdmin(clubId);
        }

        membership.updateRole(request.role());

        clubActivityLogService.log(
            clubId,
            email,
            ClubActivityLogType.MEMBER_ROLE_UPDATED,
            membership.getMember().getName() + "님의 역할이 " + request.role().name() + "(으)로 변경되었습니다.",
            "/clubs/" + clubId + "/members"
        );
    }

    @Transactional
    public void removeMember(Long clubId, Long membershipId, String email) {
        validateClubExists(clubId);
        validateClubAdmin(clubId, email);

        ClubMembership membership = clubMembershipRepository.findByIdAndClubId(clubId, membershipId)
            .orElseThrow(ClubMembershipNotFoundException::new);

        if (membership.getRole() == ClubMemberRole.ADMIN) {
            validateNotLastAdmin(clubId);
        }

        String memberName = membership.getMember().getName();
        clubMembershipRepository.delete(membership);

        clubActivityLogService.log(
            clubId,
            email,
            ClubActivityLogType.MEMBER_REMOVED,
            memberName + "님이 동아리에서 내보내기 처리되었습니다.",
            "/clubs/" + clubId + "/members"
        );
    }

    private void validateClubExists(Long clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }
    }

    private void validateClubAdmin(Long clubId, String email) {
        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("동아리 운영자만 멤버를 관리할 수 있습니다.");
        }
    }

    private void validateNotLastAdmin(Long clubId) {
        long adminCount = clubMembershipRepository.countByClub_IdAndRole(clubId, ClubMemberRole.ADMIN);

        if (adminCount <= 1) {
            throw new IllegalArgumentException("동아리에는 최소 1명의 운영자가 필요합니다.");
        }
    }
}
