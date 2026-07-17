package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubAdminRequestResponse;
import com.eulji.clubon.domain.club.entity.ClubAdminRequest;
import com.eulji.clubon.domain.club.entity.ClubAdminRequestStatus;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubMembership;
import com.eulji.clubon.domain.club.repository.ClubAdminRequestRepository;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubAdminRequestManageService {

    private final ClubAdminRequestRepository requestRepository;
    private final ClubMembershipRepository membershipRepository;

    public List<ClubAdminRequestResponse> getRequests(ClubAdminRequestStatus status) {
        List<ClubAdminRequest> requests = status == null
            ? requestRepository.findAllByOrderByCreatedAtDesc()
            : requestRepository.findByStatusOrderByCreatedAtDesc(status);

        return requests.stream().map(ClubAdminRequestResponse::from).toList();
    }

    @Transactional
    public ClubAdminRequestResponse approve(Long requestId) {
        ClubAdminRequest request = findRequest(requestId);

        ClubMembership membership = membershipRepository
            .findByClub_IdAndMember_Id(request.getClub().getId(), request.getMember().getId())
            .orElseGet(() -> membershipRepository.save(ClubMembership.builder()
                .club(request.getClub())
                .member(request.getMember())
                .role(ClubMemberRole.ADMIN)
                .build()));

        membership.updateRole(ClubMemberRole.ADMIN);
        request.getMember().changeRole(Role.ROLE_CLUB_ADMIN);
        request.approve();

        return ClubAdminRequestResponse.from(request);
    }

    @Transactional
    public ClubAdminRequestResponse reject(Long requestId, String reason) {
        ClubAdminRequest request = findRequest(requestId);
        request.reject(reason.trim());
        return ClubAdminRequestResponse.from(request);
    }

    private ClubAdminRequest findRequest(Long requestId) {
        return requestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("운영자 신청을 찾을 수 없습니다."));
    }
}
