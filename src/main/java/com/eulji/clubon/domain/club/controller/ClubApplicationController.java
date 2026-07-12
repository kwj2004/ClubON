package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.UpdateClubApplicationStatusRequest;
import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.eulji.clubon.domain.club.service.ClubApplicationService;
import com.eulji.clubon.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ClubApplicationController {

    private final ClubApplicationService clubApplicationService;

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<Void>> cancelApplication(
        @PathVariable Long applicationId,
        Authentication authentication
    ) {
        clubApplicationService.cancelApplication(applicationId, authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "가입 신청이 취소되었습니다.",
            null
        ));
    }
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponse<Void>> updateApplicationStatus(
        @PathVariable Long applicationId,
        Authentication authentication,
        @Valid @RequestBody UpdateClubApplicationStatusRequest request
    ) {
        ClubApplicationStatus status = clubApplicationService.updateApplicationStatus(
            applicationId,
            authentication.getName(),
            request
        );

        String message = status == ClubApplicationStatus.APPROVED
            ? "해당 지원자의 가입 신청이 승인(APPROVED) 되었습니다."
            : "해당 지원자의 가입 신청이 거절(REJECTED) 되었습니다.";

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            message,
            null
        ));
    }
}
