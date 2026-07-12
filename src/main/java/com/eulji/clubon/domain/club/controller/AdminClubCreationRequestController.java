package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.AdminClubCreationRequestPageResponse;
import com.eulji.clubon.domain.club.entity.ClubCreationRequestStatus;
import com.eulji.clubon.domain.club.service.ClubCreationRequestService;
import com.eulji.clubon.global.response.ApiResponse;
import com.eulji.clubon.domain.club.dto.AdminClubCreationRequestDetailResponse;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.eulji.clubon.domain.club.dto.ApproveClubCreationRequestResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import com.eulji.clubon.domain.club.dto.RejectClubCreationRequestRequest;
import com.eulji.clubon.domain.club.dto.RejectClubCreationRequestResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin/club-creation-requests")
@RequiredArgsConstructor
public class AdminClubCreationRequestController {

    private final ClubCreationRequestService clubCreationRequestService;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminClubCreationRequestPageResponse>> getClubCreationRequests(
        @RequestParam(required = false) ClubCreationRequestStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        AdminClubCreationRequestPageResponse response = clubCreationRequestService.getAdminRequests(
            status,
            page,
            size
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 개설 신청 목록 조회에 성공했습니다.",
            response
        ));
    }
    @GetMapping("/{requestId}")
    public ResponseEntity<ApiResponse<AdminClubCreationRequestDetailResponse>> getClubCreationRequestDetail(
        @PathVariable Long requestId
    ) {
        AdminClubCreationRequestDetailResponse response = clubCreationRequestService.getAdminRequestDetail(requestId);

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 개설 신청 상세 조회에 성공했습니다.",
            response
        ));
    }
    @PatchMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponse<ApproveClubCreationRequestResponse>> approveClubCreationRequest(
        @PathVariable Long requestId
    ) {
        ApproveClubCreationRequestResponse response = clubCreationRequestService.approveAdminRequest(requestId);

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 개설 신청이 승인되었습니다.",
            response
        ));
    }

    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<RejectClubCreationRequestResponse>> rejectClubCreationRequest(
        @PathVariable Long requestId,
        @Valid @RequestBody RejectClubCreationRequestRequest request
    ) {
        RejectClubCreationRequestResponse response = clubCreationRequestService.rejectAdminRequest(
            requestId,
            request
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 개설 신청이 거절되었습니다.",
            response
        ));
    }
}
