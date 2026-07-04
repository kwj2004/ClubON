package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.BookmarkedClubResponse;
import com.eulji.clubon.domain.club.dto.ClubBookmarkResponse;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubBookmark;
import com.eulji.clubon.domain.club.repository.ClubBookmarkRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.ClubNotFoundException;
import com.eulji.clubon.global.error.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubBookmarkService {

    private final ClubBookmarkRepository clubBookmarkRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ClubBookmarkResponse toggleBookmark(Long clubId, String email) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(ClubNotFoundException::new);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        return clubBookmarkRepository.findByClub_IdAndMember_Email(clubId, email)
                .map(bookmark -> {
                    clubBookmarkRepository.delete(bookmark);
                    return new ClubBookmarkResponse(clubId, false);
                })
                .orElseGet(() -> {
                    clubBookmarkRepository.save(ClubBookmark.builder()
                            .club(club)
                            .member(member)
                            .build());
                    return new ClubBookmarkResponse(clubId, true);
                });
    }

    @Transactional
    public ClubBookmarkResponse deleteBookmark(Long clubId, String email) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        clubBookmarkRepository.findByClub_IdAndMember_Email(clubId, email)
                .ifPresent(clubBookmarkRepository::delete);

        return new ClubBookmarkResponse(clubId, false);
    }

    public List<BookmarkedClubResponse> getMyBookmarks(String email) {
        return clubBookmarkRepository.findMyBookmarks(email)
                .stream()
                .map(bookmark -> BookmarkedClubResponse.from(bookmark.getClub()))
                .toList();
    }

    public boolean isBookmarked(Long clubId, String email) {
        return clubBookmarkRepository.existsByClub_IdAndMember_Email(clubId, email);
    }
}
