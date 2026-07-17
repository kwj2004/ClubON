package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.ClubAdminRequestResponse;
import com.eulji.clubon.domain.club.dto.RejectClubAdminRequest;
import com.eulji.clubon.domain.club.entity.ClubAdminRequestStatus;
import com.eulji.clubon.domain.club.service.ClubAdminRequestManageService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/club-admin-requests")
@RequiredArgsConstructor
public class AdminClubAdminRequestController {

    private final ClubAdminRequestManageService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClubAdminRequestResponse>>> getRequests(
        @RequestParam(required = false) ClubAdminRequestStatus status
    ) {
        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "운영자 신청 목록 조회 성공",
            service.getRequests(status)
        ));
    }

    @PatchMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponse<ClubAdminRequestResponse>> approve(@PathVariable Long requestId) {
        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "운영자 신청이 승인되었습니다.",
            service.approve(requestId)
        ));
    }

    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<ClubAdminRequestResponse>> reject(
        @PathVariable Long requestId,
        @Valid @RequestBody RejectClubAdminRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "운영자 신청이 거절되었습니다.",
            service.reject(requestId, request.reason())
        ));
    }
}
