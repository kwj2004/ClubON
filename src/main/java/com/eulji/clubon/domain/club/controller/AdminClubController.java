package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.AdminClubListResponse;
import com.eulji.clubon.domain.club.dto.AdminClubStatisticsResponse;
import com.eulji.clubon.domain.club.entity.ClubType;
import com.eulji.clubon.domain.club.service.AdminClubService;
import com.eulji.clubon.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/clubs")
@RequiredArgsConstructor
public class AdminClubController {

    private final AdminClubService adminClubService;

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<AdminClubStatisticsResponse>> getClubStatistics() {
        AdminClubStatisticsResponse response = adminClubService.getClubStatistics();

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 통계 조회 성공",
            response
        ));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminClubListResponse>>> getAdminClubs(
        @RequestParam(required = false) ClubType type,
        @RequestParam(required = false) Boolean isActive
    ) {
        List<AdminClubListResponse> response = adminClubService.getAdminClubs(type, isActive);

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "관리자용 동아리 목록 조회 성공",
            response
        ));
    }
}
