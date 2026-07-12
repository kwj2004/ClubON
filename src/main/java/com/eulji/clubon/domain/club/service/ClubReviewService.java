package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubReviewListResponse;
import com.eulji.clubon.domain.club.dto.ClubReviewPageResponse;
import com.eulji.clubon.domain.club.dto.CreateClubReviewRequest;
import com.eulji.clubon.domain.club.dto.CreateClubReviewResponse;
import com.eulji.clubon.domain.club.dto.MyClubReviewListResponse;
import com.eulji.clubon.domain.club.dto.MyClubReviewPageResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubReviewRequest;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubReview;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.domain.club.repository.ClubReviewRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.ClubNotFoundException;
import com.eulji.clubon.global.error.ClubReviewNotFoundException;
import com.eulji.clubon.global.error.DuplicateClubReviewException;
import com.eulji.clubon.global.error.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubReviewService {

    private final ClubReviewRepository clubReviewRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMembershipRepository clubMembershipRepository;

    @Transactional
    public CreateClubReviewResponse createReview(
        Long clubId,
        String email,
        CreateClubReviewRequest request
    ) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(ClubNotFoundException::new);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);

        validateClubMember(clubId, email);
        validateNotDuplicate(clubId, email);

        ClubReview review = clubReviewRepository.save(ClubReview.builder()
            .club(club)
            .member(member)
            .rating(request.rating())
            .content(request.content())
            .build());

        return CreateClubReviewResponse.from(review);
    }

    public ClubReviewPageResponse getReviews(Long clubId, int page, int size) {
        validatePageRequest(page, size);

        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        return ClubReviewPageResponse.from(
            clubReviewRepository.findByClubId(clubId, PageRequest.of(page, size))
                .map(ClubReviewListResponse::from)
        );
    }

    public MyClubReviewPageResponse getMyReviews(String email, int page, int size) {
        validatePageRequest(page, size);

        if (!memberRepository.existsByEmail(email)) {
            throw new MemberNotFoundException();
        }

        return MyClubReviewPageResponse.from(
            clubReviewRepository.findByMemberEmail(email, PageRequest.of(page, size))
                .map(MyClubReviewListResponse::from)
        );
    }

    @Transactional
    public void updateReview(
        Long clubId,
        Long reviewId,
        String email,
        UpdateClubReviewRequest request
    ) {
        validateUpdateRequest(request);

        ClubReview review = clubReviewRepository.findOwnedReview(clubId, reviewId, email)
            .orElseThrow(ClubReviewNotFoundException::new);

        review.update(request.rating(), request.content());
    }

    @Transactional
    public void deleteReview(Long clubId, Long reviewId, String email) {
        ClubReview review = clubReviewRepository.findOwnedReview(clubId, reviewId, email)
            .orElseThrow(ClubReviewNotFoundException::new);

        clubReviewRepository.delete(review);
    }

    private void validateClubMember(Long clubId, String email) {
        boolean isClubMember = clubMembershipRepository.existsByClub_IdAndMember_Email(clubId, email);

        if (!isClubMember) {
            throw new AccessDeniedException("해당 동아리 멤버만 후기를 작성할 수 있습니다.");
        }
    }

    private void validateNotDuplicate(Long clubId, String email) {
        if (clubReviewRepository.existsByClub_IdAndMember_Email(clubId, email)) {
            throw new DuplicateClubReviewException();
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

    private void validateUpdateRequest(UpdateClubReviewRequest request) {
        if (request.rating() == null && request.content() == null) {
            throw new IllegalArgumentException("수정할 후기 정보를 입력해주세요.");
        }
        if (request.content() != null && !StringUtils.hasText(request.content())) {
            throw new IllegalArgumentException("후기 내용을 입력해주세요.");
        }
    }
}
