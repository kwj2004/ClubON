package com.eulji.clubon.domain.member.controller;

import com.eulji.clubon.domain.club.dto.MyClubApplicationResponse;
import com.eulji.clubon.domain.club.dto.MyClubPostPageResponse;
import com.eulji.clubon.domain.club.dto.MyClubReviewPageResponse;
import com.eulji.clubon.domain.club.entity.ClubPostCategory;
import com.eulji.clubon.domain.club.service.ClubApplicationService;
import com.eulji.clubon.domain.club.dto.BookmarkedClubResponse;
import com.eulji.clubon.domain.club.dto.MyClubResponse;
import com.eulji.clubon.domain.club.service.ClubBookmarkService;
import com.eulji.clubon.domain.club.service.ClubMembershipService;
import com.eulji.clubon.domain.club.service.MyClubPostService;
import com.eulji.clubon.domain.club.service.ClubReviewService;
import com.eulji.clubon.domain.member.dto.MyProfileResponse;
import com.eulji.clubon.domain.member.dto.UpdateMyProfileRequest;
import com.eulji.clubon.domain.member.dto.UpdateMyProfileResponse;
import com.eulji.clubon.domain.member.dto.WithdrawMemberRequest;
import com.eulji.clubon.domain.member.service.MemberService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ClubMembershipService clubMembershipService;
    private final ClubBookmarkService clubBookmarkService;
    private final ClubApplicationService clubApplicationService;
    private final MyClubPostService myClubPostService;
    private final ClubReviewService clubReviewService;

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> withdraw(
            Authentication authentication,
            @Valid @RequestBody WithdrawMemberRequest request
    ) {
        memberService.withdraw(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "회원 탈퇴가 완료되었습니다.",
                null
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile(Authentication authentication) {
        MyProfileResponse response = memberService.getMyProfile(authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "프로필 조회를 성공했습니다.",
                response
        ));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UpdateMyProfileResponse>> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateMyProfileRequest request
    ) {
        UpdateMyProfileResponse response = memberService.updateMyProfile(authentication.getName(), request);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "프로필 정보가 성공적으로 수정되었습니다.",
                response
        ));
    }

    @GetMapping("/me/clubs")
    public ResponseEntity<ApiResponse<List<MyClubResponse>>> getMyClubs(Authentication authentication) {
        List<MyClubResponse> response = clubMembershipService.getMyClubs(authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "가입된 동아리 목록 조회 성공",
                response
        ));
    }

    @GetMapping("/me/bookmarks")
    public ResponseEntity<ApiResponse<List<BookmarkedClubResponse>>> getMyBookmarks(Authentication authentication) {
        List<BookmarkedClubResponse> response = clubBookmarkService.getMyBookmarks(authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "관심 동아리 목록 조회 성공",
                response
        ));
    }
    @GetMapping("/me/applications")
    public ResponseEntity<ApiResponse<List<MyClubApplicationResponse>>> getMyApplications(Authentication authentication) {
        List<MyClubApplicationResponse> response = clubApplicationService.getMyApplications(authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "내 가입 신청 내역 조회 성공",
            response
        ));
    }

    @GetMapping("/me/posts")
    public ResponseEntity<ApiResponse<MyClubPostPageResponse>> getMyPosts(
            Authentication authentication,
            @RequestParam(required = false) ClubPostCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        MyClubPostPageResponse response = myClubPostService.getMyPosts(
                authentication.getName(),
                category,
                page,
                size
        );

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "내가 쓴 게시글 목록 조회 성공",
                response
        ));
    }

    @GetMapping("/me/reviews")
    public ResponseEntity<ApiResponse<MyClubReviewPageResponse>> getMyReviews(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        MyClubReviewPageResponse response = clubReviewService.getMyReviews(
                authentication.getName(),
                page,
                size
        );

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "내 후기 목록 조회 성공",
                response
        ));
    }
}
