package com.eulji.clubon.domain.member.controller;

import com.eulji.clubon.domain.club.dto.BookmarkedClubResponse;
import com.eulji.clubon.domain.club.dto.MyClubResponse;
import com.eulji.clubon.domain.club.service.ClubBookmarkService;
import com.eulji.clubon.domain.club.service.ClubMembershipService;
import com.eulji.clubon.domain.member.dto.MyProfileResponse;
import com.eulji.clubon.domain.member.dto.UpdateMyProfileRequest;
import com.eulji.clubon.domain.member.dto.UpdateMyProfileResponse;
import com.eulji.clubon.domain.member.service.MemberService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ClubMembershipService clubMembershipService;
    private final ClubBookmarkService clubBookmarkService;

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
}
