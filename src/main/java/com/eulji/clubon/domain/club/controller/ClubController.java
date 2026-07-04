package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.ClubDetailResponse;
import com.eulji.clubon.domain.club.dto.ClubListResponse;
import com.eulji.clubon.domain.club.dto.ClubBookmarkResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubRequest;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import com.eulji.clubon.domain.club.entity.ClubType;
import com.eulji.clubon.domain.club.service.ClubBookmarkService;
import com.eulji.clubon.domain.club.service.ClubService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final ClubBookmarkService clubBookmarkService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClubListResponse>>> getClubs(
            @RequestParam(required = false) ClubType type,
            @RequestParam(required = false) ClubStatus status,
            @Size(max = 50) @RequestParam(required = false) String keyword
    ) {
        List<ClubListResponse> response = clubService.getClubs(type, status, keyword);

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "동아리 목록 조회 성공",
                response
        ));
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<ApiResponse<ClubDetailResponse>> getClubDetail(
            @PathVariable Long clubId,
            Authentication authentication
    ) {
        ClubDetailResponse response = clubService.getClubDetail(clubId, authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "동아리 상세 정보 조회 성공",
                response
        ));
    }

    @PatchMapping("/{clubId}")
    public ResponseEntity<ApiResponse<Void>> updateClub(
            @PathVariable Long clubId,
            Authentication authentication,
            @Valid @RequestBody UpdateClubRequest request
    ) {
        clubService.updateClub(clubId, request, authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "동아리 정보가 성공적으로 수정되었습니다.",
                null
        ));
    }

    @PostMapping("/{clubId}/bookmarks")
    public ResponseEntity<ApiResponse<ClubBookmarkResponse>> toggleBookmark(
            @PathVariable Long clubId,
            Authentication authentication
    ) {
        ClubBookmarkResponse response = clubBookmarkService.toggleBookmark(clubId, authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Bookmark state changed.",
                response
        ));
    }

    @DeleteMapping("/{clubId}/bookmarks")
    public ResponseEntity<ApiResponse<ClubBookmarkResponse>> deleteBookmark(
            @PathVariable Long clubId,
            Authentication authentication
    ) {
        ClubBookmarkResponse response = clubBookmarkService.deleteBookmark(clubId, authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "Bookmark removed.",
                response
        ));
    }
}
