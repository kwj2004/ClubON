package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.ClubPostDetailResponse;
import com.eulji.clubon.domain.club.dto.ClubPostPageResponse;
import com.eulji.clubon.domain.club.dto.CreateClubPostRequest;
import com.eulji.clubon.domain.club.dto.CreateClubPostResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubPostRequest;
import com.eulji.clubon.domain.club.entity.ClubPostCategory;
import com.eulji.clubon.domain.club.service.ClubPostService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubPostController {

    private final ClubPostService clubPostService;

    @PostMapping("/api/clubs/{clubId}/posts")
    public ResponseEntity<ApiResponse<CreateClubPostResponse>> createPost(
        @PathVariable Long clubId,
        Authentication authentication,
        @Valid @RequestBody CreateClubPostRequest request
    ) {
        CreateClubPostResponse response = clubPostService.createPost(
            clubId,
            authentication.getName(),
            request
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.of(
                HttpStatus.CREATED.value(),
                "게시글이 성공적으로 작성되었습니다.",
                response
            ));
    }

    @GetMapping("/api/clubs/{clubId}/posts")
    public ResponseEntity<ApiResponse<ClubPostPageResponse>> getPosts(
        @PathVariable Long clubId,
        @RequestParam(required = false) ClubPostCategory category,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        ClubPostPageResponse response = clubPostService.getPosts(
            clubId,
            category,
            keyword,
            page,
            size
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "게시글 목록 조회 성공",
            response
        ));
    }

    @GetMapping("/api/clubs/{clubId}/posts/{postId}")
    public ResponseEntity<ApiResponse<ClubPostDetailResponse>> getPostDetail(
        @PathVariable Long clubId,
        @PathVariable Long postId
    ) {
        ClubPostDetailResponse response = clubPostService.getPostDetail(clubId, postId);

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "게시글 상세 조회 성공",
            response
        ));
    }

    @GetMapping("/api/clubs/{clubId}/posts/drafts")
    public ResponseEntity<ApiResponse<ClubPostPageResponse>> getMyDraftPosts(
        @PathVariable Long clubId,
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        ClubPostPageResponse response = clubPostService.getMyDraftPosts(
            clubId,
            authentication.getName(),
            page,
            size
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "임시저장 게시글 목록 조회 성공",
            response
        ));
    }

    @PatchMapping("/api/clubs/{clubId}/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(
        @PathVariable Long clubId,
        @PathVariable Long postId,
        Authentication authentication,
        @Valid @RequestBody UpdateClubPostRequest request
    ) {
        clubPostService.updatePost(
            clubId,
            postId,
            authentication.getName(),
            request
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "게시글이 성공적으로 수정되었습니다.",
            null
        ));
    }

    @DeleteMapping("/api/clubs/{clubId}/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
        @PathVariable Long clubId,
        @PathVariable Long postId,
        Authentication authentication
    ) {
        clubPostService.deletePost(
            clubId,
            postId,
            authentication.getName()
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "게시글이 삭제되었습니다.",
            null
        ));
    }
}
