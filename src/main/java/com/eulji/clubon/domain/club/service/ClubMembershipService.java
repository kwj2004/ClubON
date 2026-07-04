package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.MyClubResponse;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMembershipService {

    private final ClubMembershipRepository clubMembershipRepository;

    public List<MyClubResponse> getMyClubs(String email) {
        return clubMembershipRepository.findMyClubMemberships(email)
                .stream()
                .map(MyClubResponse::from)
                .toList();
    }
}
