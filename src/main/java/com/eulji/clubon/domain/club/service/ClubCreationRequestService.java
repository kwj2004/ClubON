package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubCreationRequestDetailResponse;
import com.eulji.clubon.domain.club.dto.ClubCreationRequestListResponse;
import com.eulji.clubon.domain.club.dto.CreateClubRequest;
import com.eulji.clubon.domain.club.dto.CreateClubResponse;
import com.eulji.clubon.domain.club.entity.ClubCreationRequest;
import com.eulji.clubon.domain.club.entity.ClubCreationRequestStatus;
import com.eulji.clubon.domain.club.repository.ClubCreationRequestRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.ClubCreationRequestNotFoundException;
import com.eulji.clubon.global.error.DuplicateClubNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubCreationRequestService {

    private final ClubRepository clubRepository;
    private final ClubCreationRequestRepository clubCreationRequestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateClubResponse createRequest(CreateClubRequest request, String email) {
        String normalizedName = request.name().trim();

        if (clubRepository.existsByName(normalizedName)
                || clubCreationRequestRepository.existsByNameAndStatus(normalizedName, ClubCreationRequestStatus.PENDING)) {
            throw new DuplicateClubNameException();
        }

        Member requester = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        ClubCreationRequest creationRequest = clubCreationRequestRepository.save(ClubCreationRequest.builder()
                .requester(requester)
                .name(normalizedName)
                .type(request.type())
                .shortDescription(request.shortDescription().trim())
                .fullDescription(request.fullDescription().trim())
                .build());

        return CreateClubResponse.from(creationRequest);
    }

    public List<ClubCreationRequestListResponse> getMyRequests(String email) {
        return clubCreationRequestRepository.findByRequester_EmailOrderByCreatedAtDesc(email)
                .stream()
                .map(ClubCreationRequestListResponse::from)
                .toList();
    }

    public ClubCreationRequestDetailResponse getMyRequestDetail(Long requestId, String email) {
        return clubCreationRequestRepository.findByIdAndRequester_Email(requestId, email)
                .map(ClubCreationRequestDetailResponse::from)
                .orElseThrow(ClubCreationRequestNotFoundException::new);
    }
}
