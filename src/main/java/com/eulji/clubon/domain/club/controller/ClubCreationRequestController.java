package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.ClubCreationRequestDetailResponse;
import com.eulji.clubon.domain.club.dto.ClubCreationRequestListResponse;
import com.eulji.clubon.domain.club.dto.CreateClubRequest;
import com.eulji.clubon.domain.club.dto.CreateClubResponse;
import com.eulji.clubon.domain.club.service.ClubCreationRequestService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/club-creation-requests")
@RequiredArgsConstructor
public class ClubCreationRequestController {

    private final ClubCreationRequestService clubCreationRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateClubResponse>> createClubCreationRequest(
            Authentication authentication,
            @Valid @RequestBody CreateClubRequest request
    ) {
        CreateClubResponse response = clubCreationRequestService.createRequest(request, authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "동아리 개설 신청이 완료되었습니다.",
                        response
                ));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ClubCreationRequestListResponse>>> getMyClubCreationRequests(
            Authentication authentication
    ) {
        List<ClubCreationRequestListResponse> response = clubCreationRequestService.getMyRequests(authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "내 동아리 개설 신청 목록 조회에 성공했습니다.",
                response
        ));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponse<ClubCreationRequestDetailResponse>> getMyClubCreationRequestDetail(
            @PathVariable Long requestId,
            Authentication authentication
    ) {
        ClubCreationRequestDetailResponse response = clubCreationRequestService.getMyRequestDetail(
                requestId,
                authentication.getName()
        );

        return ResponseEntity.ok(ApiResponse.of(
                HttpStatus.OK.value(),
                "동아리 개설 신청 상세 조회에 성공했습니다.",
                response
        ));
    }
}
