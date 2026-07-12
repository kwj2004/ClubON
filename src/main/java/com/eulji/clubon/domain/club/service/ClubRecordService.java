package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.CreateClubRecordRequest;
import com.eulji.clubon.domain.club.dto.CreateClubRecordResponse;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubRecord;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubRecordRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.global.error.ClubNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eulji.clubon.domain.club.dto.ClubRecordListResponse;

import java.util.List;
import com.eulji.clubon.domain.club.dto.ClubRecordDetailResponse;
import com.eulji.clubon.global.error.ClubRecordNotFoundException;
import com.eulji.clubon.domain.club.dto.UpdateClubRecordRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubRecordService {

    private final ClubRecordRepository clubRecordRepository;
    private final ClubRepository clubRepository;
    private final ClubMembershipRepository clubMembershipRepository;

    @Transactional
    public CreateClubRecordResponse createRecord(
        Long clubId,
        String email,
        CreateClubRecordRequest request
    ) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(ClubNotFoundException::new);

        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("해당 동아리의 활동 기록을 작성할 권한이 없습니다.");
        }

        ClubRecord record = clubRecordRepository.save(ClubRecord.builder()
            .club(club)
            .title(request.title())
            .content(request.content())
            .imageUrls(request.imageUrls())
            .build());

        return CreateClubRecordResponse.from(record);
    }
    public List<ClubRecordListResponse> getRecords(Long clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        return clubRecordRepository.findByClub_IdOrderByCreatedAtDesc(clubId)
            .stream()
            .map(ClubRecordListResponse::from)
            .toList();
    }
    public ClubRecordDetailResponse getRecordDetail(Long clubId, Long recordId) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        ClubRecord record = clubRecordRepository.findByIdAndClubId(recordId, clubId)
            .orElseThrow(ClubRecordNotFoundException::new);

        return ClubRecordDetailResponse.from(record);
    }
    @Transactional
    public void updateRecord(
        Long clubId,
        Long recordId,
        String email,
        UpdateClubRecordRequest request
    ) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("해당 동아리의 활동 기록을 수정할 권한이 없습니다.");
        }

        ClubRecord record = clubRecordRepository.findByIdAndClubId(recordId, clubId)
            .orElseThrow(ClubRecordNotFoundException::new);

        record.update(
            request.title(),
            request.content(),
            request.imageUrls()
        );
    }
    @Transactional
    public void deleteRecord(Long clubId, Long recordId, String email) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("해당 동아리의 활동 기록을 삭제할 권한이 없습니다.");
        }

        ClubRecord record = clubRecordRepository.findByIdAndClubId(recordId, clubId)
            .orElseThrow(ClubRecordNotFoundException::new);

        clubRecordRepository.delete(record);
    }
}
