package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubActivityLogPageResponse;
import com.eulji.clubon.domain.club.dto.ClubActivityLogResponse;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubActivityLog;
import com.eulji.clubon.domain.club.entity.ClubActivityLogType;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.repository.ClubActivityLogRepository;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.ClubNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubActivityLogService {

    private final ClubActivityLogRepository clubActivityLogRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMembershipRepository clubMembershipRepository;

    public ClubActivityLogPageResponse getActivityLogs(
        Long clubId,
        String email,
        int page,
        int size
    ) {
        validatePageRequest(page, size);
        validateClubAdmin(clubId, email);

        return ClubActivityLogPageResponse.from(
            clubActivityLogRepository.findByClubId(clubId, PageRequest.of(page, size))
                .map(ClubActivityLogResponse::from)
        );
    }

    @Transactional
    public void log(
        Long clubId,
        String actorEmail,
        ClubActivityLogType type,
        String message,
        String linkUrl
    ) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(ClubNotFoundException::new);
        Member actor = actorEmail == null
            ? null
            : memberRepository.findByEmail(actorEmail).orElse(null);

        clubActivityLogRepository.save(ClubActivityLog.builder()
            .club(club)
            .actor(actor)
            .type(type)
            .message(message)
            .linkUrl(linkUrl)
            .build());
    }

    private void validateClubAdmin(Long clubId, String email) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("동아리 운영자만 최근 활동을 조회할 수 있습니다.");
        }
    }

    private void validatePageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0 이상이어야 합니다.");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("페이지 크기는 1 이상 100 이하로 입력해주세요.");
        }
    }
}
