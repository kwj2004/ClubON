package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.ClubReviewPageResponse;
import com.eulji.clubon.domain.club.dto.CreateClubReviewRequest;
import com.eulji.clubon.domain.club.dto.CreateClubReviewResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubReviewRequest;
import com.eulji.clubon.domain.club.service.ClubReviewService;
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
public class ClubReviewController {

    private final ClubReviewService clubReviewService;

    @PostMapping("/api/clubs/{clubId}/reviews")
    public ResponseEntity<ApiResponse<CreateClubReviewResponse>> createReview(
        @PathVariable Long clubId,
        Authentication authentication,
        @Valid @RequestBody CreateClubReviewRequest request
    ) {
        CreateClubReviewResponse response = clubReviewService.createReview(
            clubId,
            authentication.getName(),
            request
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.of(
                HttpStatus.CREATED.value(),
                "후기가 성공적으로 작성되었습니다.",
                response
            ));
    }

    @GetMapping("/api/clubs/{clubId}/reviews")
    public ResponseEntity<ApiResponse<ClubReviewPageResponse>> getReviews(
        @PathVariable Long clubId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        ClubReviewPageResponse response = clubReviewService.getReviews(clubId, page, size);

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "후기 목록 조회 성공",
            response
        ));
    }

    @PatchMapping("/api/clubs/{clubId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> updateReview(
        @PathVariable Long clubId,
        @PathVariable Long reviewId,
        Authentication authentication,
        @Valid @RequestBody UpdateClubReviewRequest request
    ) {
        clubReviewService.updateReview(
            clubId,
            reviewId,
            authentication.getName(),
            request
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "후기가 성공적으로 수정되었습니다.",
            null
        ));
    }

    @DeleteMapping("/api/clubs/{clubId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
        @PathVariable Long clubId,
        @PathVariable Long reviewId,
        Authentication authentication
    ) {
        clubReviewService.deleteReview(
            clubId,
            reviewId,
            authentication.getName()
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "후기가 삭제되었습니다.",
            null
        ));
    }
}
