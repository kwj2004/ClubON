package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.ClubApplicationFormResponse;
import com.eulji.clubon.domain.club.service.ClubApplicationService;
import com.eulji.clubon.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs/{clubId}/application-form")
@RequiredArgsConstructor
public class ClubApplicationFormController {

    private final ClubApplicationService clubApplicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<ClubApplicationFormResponse>>
    getApplicationForm(
        @PathVariable Long clubId,
        Authentication authentication
    ) {

        ClubApplicationFormResponse response =
            clubApplicationService.getApplicationForm(
                clubId,
                authentication.getName()
            );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "가입 신청서 양식 조회에 성공했습니다.",
            response
        ));
    }
}
