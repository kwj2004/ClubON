package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubPostDetailResponse;
import com.eulji.clubon.domain.club.dto.ClubPostPageResponse;
import com.eulji.clubon.domain.club.dto.CreateClubPostRequest;
import com.eulji.clubon.domain.club.dto.CreateClubPostResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubPostRequest;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubActivityLogType;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubPost;
import com.eulji.clubon.domain.club.entity.ClubPostCategory;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubPostRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.domain.notification.entity.NotificationType;
import com.eulji.clubon.domain.notification.service.NotificationService;
import com.eulji.clubon.global.error.ClubNotFoundException;
import com.eulji.clubon.global.error.ClubPostNotFoundException;
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
public class ClubPostService {

    private final ClubPostRepository clubPostRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMembershipRepository clubMembershipRepository;
    private final NotificationService notificationService;
    private final ClubActivityLogService clubActivityLogService;

    @Transactional
    public CreateClubPostResponse createPost(
        Long clubId,
        String email,
        CreateClubPostRequest request
    ) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(ClubNotFoundException::new);

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);

        validateWritableClubMember(clubId, email);
        validateNoticePermission(clubId, email, request.category());

        ClubPost post = clubPostRepository.save(ClubPost.builder()
            .club(club)
            .author(member)
            .category(request.category())
            .status(request.status())
            .title(request.title())
            .content(request.content())
            .attachmentUrls(request.attachmentUrls())
            .build());

        notificationService.createNotifications(
            clubMembershipRepository.findMembersByClubIdExceptEmail(clubId, email),
            NotificationType.CLUB_POST,
            club.getName() + "에 새 게시글이 등록되었습니다.",
            post.getTitle(),
            "/clubs/" + clubId + "/posts/" + post.getId()
        );

        clubActivityLogService.log(
            clubId,
            email,
            ClubActivityLogType.POST_CREATED,
            post.getTitle() + " 게시글이 등록되었습니다.",
            "/clubs/" + clubId + "/posts/" + post.getId()
        );

        return CreateClubPostResponse.from(post);
    }

    public ClubPostPageResponse getPosts(
        Long clubId,
        ClubPostCategory category,
        String keyword,
        int page,
        int size
    ) {
        validatePageRequest(page, size);

        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;

        return ClubPostPageResponse.from(
            clubPostRepository.findPublishedPosts(
                    clubId,
                    category,
                    normalizedKeyword,
                    PageRequest.of(page, size)
                )
                .map(com.eulji.clubon.domain.club.dto.ClubPostListResponse::from)
        );
    }

    @Transactional
    public ClubPostDetailResponse getPostDetail(Long clubId, Long postId) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        ClubPost post = clubPostRepository.findPublishedPostDetail(clubId, postId)
            .orElseThrow(ClubPostNotFoundException::new);

        post.increaseViewCount();

        return ClubPostDetailResponse.from(post);
    }

    public ClubPostPageResponse getMyDraftPosts(
        Long clubId,
        String email,
        int page,
        int size
    ) {
        validatePageRequest(page, size);

        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        validateWritableClubMember(clubId, email);

        return ClubPostPageResponse.from(
            clubPostRepository.findMyDraftPosts(
                    clubId,
                    email,
                    PageRequest.of(page, size)
                )
                .map(com.eulji.clubon.domain.club.dto.ClubPostListResponse::from)
        );
    }

    @Transactional
    public void updatePost(
        Long clubId,
        Long postId,
        String email,
        UpdateClubPostRequest request
    ) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        ClubPost post = clubPostRepository.findByClubIdAndPostId(clubId, postId)
            .orElseThrow(ClubPostNotFoundException::new);

        validatePostManagePermission(clubId, email, post);
        validateUpdateRequest(request);

        ClubPostCategory nextCategory = request.category() == null
            ? post.getCategory()
            : request.category();
        validateNoticePermission(clubId, email, nextCategory);

        post.update(
            request.category(),
            request.status(),
            request.title(),
            request.content(),
            request.attachmentUrls()
        );

        clubActivityLogService.log(
            clubId,
            email,
            ClubActivityLogType.POST_UPDATED,
            post.getTitle() + " 게시글이 수정되었습니다.",
            "/clubs/" + clubId + "/posts/" + post.getId()
        );
    }

    @Transactional
    public void deletePost(Long clubId, Long postId, String email) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        ClubPost post = clubPostRepository.findByClubIdAndPostId(clubId, postId)
            .orElseThrow(ClubPostNotFoundException::new);

        validatePostManagePermission(clubId, email, post);

        String title = post.getTitle();
        clubPostRepository.delete(post);

        clubActivityLogService.log(
            clubId,
            email,
            ClubActivityLogType.POST_DELETED,
            title + " 게시글이 삭제되었습니다.",
            "/clubs/" + clubId + "/posts"
        );
    }

    private void validatePageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0 이상이어야 합니다.");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("페이지 크기는 1 이상 100 이하로 입력해주세요.");
        }
    }

    private void validateWritableClubMember(Long clubId, String email) {
        boolean isClubMember = clubMembershipRepository.existsByClub_IdAndMember_Email(clubId, email);

        if (!isClubMember) {
            throw new AccessDeniedException("해당 동아리 회원만 게시글을 작성할 수 있습니다.");
        }
    }

    private void validateNoticePermission(Long clubId, String email, ClubPostCategory category) {
        if (category != ClubPostCategory.NOTICE) {
            return;
        }

        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("공지 게시글은 동아리 운영진만 작성할 수 있습니다.");
        }
    }

    private void validatePostManagePermission(Long clubId, String email, ClubPost post) {
        boolean isAuthor = post.isAuthor(email);
        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isAuthor && !isClubAdmin) {
            throw new AccessDeniedException("게시글 작성자 또는 동아리 운영진만 수정할 수 있습니다.");
        }
    }

    private void validateUpdateRequest(UpdateClubPostRequest request) {
        if (request.title() != null && !StringUtils.hasText(request.title())) {
            throw new IllegalArgumentException("게시글 제목을 입력해주세요.");
        }
        if (request.content() != null && !StringUtils.hasText(request.content())) {
            throw new IllegalArgumentException("게시글 내용을 입력해주세요.");
        }
    }
}
