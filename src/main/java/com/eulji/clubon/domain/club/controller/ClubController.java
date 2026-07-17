package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.ClubApplicationApplicantResponse;
import com.eulji.clubon.domain.club.dto.ClubActivityLogPageResponse;
import com.eulji.clubon.domain.club.dto.ClubAdminDashboardResponse;
import com.eulji.clubon.domain.club.dto.ClubMemberResponse;
import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;
import com.eulji.clubon.domain.club.dto.CreateClubApplicationRequest;
import com.eulji.clubon.domain.club.dto.CreateClubApplicationResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubMemberRoleRequest;
import com.eulji.clubon.domain.club.service.ClubApplicationService;
import com.eulji.clubon.domain.club.dto.ClubDetailResponse;
import com.eulji.clubon.domain.club.dto.ClubListResponse;
import com.eulji.clubon.domain.club.dto.ClubBookmarkResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubRequest;
import com.eulji.clubon.domain.club.entity.ClubCategory;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import com.eulji.clubon.domain.club.entity.ClubType;
import com.eulji.clubon.domain.club.service.ClubBookmarkService;
import com.eulji.clubon.domain.club.service.ClubActivityLogService;
import com.eulji.clubon.domain.club.service.ClubAdminDashboardService;
import com.eulji.clubon.domain.club.service.ClubMemberManageService;
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
    private final ClubApplicationService clubApplicationService;
    private final ClubAdminDashboardService clubAdminDashboardService;
    private final ClubMemberManageService clubMemberManageService;
    private final ClubActivityLogService clubActivityLogService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClubListResponse>>> getClubs(
        @RequestParam(required = false) ClubType type,
        @RequestParam(required = false) ClubCategory category,
        @RequestParam(required = false) ClubStatus status,
        @Size(max = 50) @RequestParam(required = false) String keyword
    ) {
        List<ClubListResponse> response = clubService.getClubs(type, category, status, keyword);

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
        String email = authentication == null ? null : authentication.getName();
        ClubDetailResponse response = clubService.getClubDetail(clubId, email);

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

    @GetMapping("/{clubId}/dashboard")
    public ResponseEntity<ApiResponse<ClubAdminDashboardResponse>> getAdminDashboard(
        @PathVariable Long clubId,
        Authentication authentication
    ) {
        ClubAdminDashboardResponse response = clubAdminDashboardService.getDashboard(
            clubId,
            authentication.getName()
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "운영 동아리 대시보드 통계 조회 성공",
            response
        ));
    }

    @GetMapping("/{clubId}/members")
    public ResponseEntity<ApiResponse<List<ClubMemberResponse>>> getMembers(
        @PathVariable Long clubId,
        @RequestParam(required = false) String keyword,
        Authentication authentication
    ) {
        List<ClubMemberResponse> response = clubMemberManageService.getMembers(
            clubId,
            authentication.getName(),
            keyword
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 멤버 목록 조회 성공",
            response
        ));
    }

    @GetMapping("/{clubId}/activity-logs")
    public ResponseEntity<ApiResponse<ClubActivityLogPageResponse>> getActivityLogs(
        @PathVariable Long clubId,
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        ClubActivityLogPageResponse response = clubActivityLogService.getActivityLogs(
            clubId,
            authentication.getName(),
            page,
            size
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 최근 활동 조회 성공",
            response
        ));
    }

    @PatchMapping("/{clubId}/members/{membershipId}/role")
    public ResponseEntity<ApiResponse<Void>> updateMemberRole(
        @PathVariable Long clubId,
        @PathVariable Long membershipId,
        Authentication authentication,
        @Valid @RequestBody UpdateClubMemberRoleRequest request
    ) {
        clubMemberManageService.updateMemberRole(
            clubId,
            membershipId,
            authentication.getName(),
            request
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 멤버 역할이 변경되었습니다.",
            null
        ));
    }

    @DeleteMapping("/{clubId}/members/{membershipId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
        @PathVariable Long clubId,
        @PathVariable Long membershipId,
        Authentication authentication
    ) {
        clubMemberManageService.removeMember(
            clubId,
            membershipId,
            authentication.getName()
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "동아리 멤버가 내보내기 처리되었습니다.",
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

    @PostMapping("/{clubId}/applications")
    public ResponseEntity<ApiResponse<CreateClubApplicationResponse>> createApplication(
        @PathVariable Long clubId,
        Authentication authentication,
        @Valid @RequestBody CreateClubApplicationRequest request
    ) {
        CreateClubApplicationResponse response = clubApplicationService.createApplication(
            clubId,
            authentication.getName(),
            request
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.of(
                HttpStatus.CREATED.value(),
                "가입 신청이 성공적으로 완료되었습니다.",
                response
            ));
    }
    @GetMapping("/{clubId}/applications")
    public ResponseEntity<ApiResponse<List<ClubApplicationApplicantResponse>>> getClubApplications(
        @PathVariable Long clubId,
        @RequestParam(required = false) ClubApplicationStatus status,
        Authentication authentication
    ) {
        List<ClubApplicationApplicantResponse> response = clubApplicationService.getClubApplications(
            clubId,
            status,
            authentication.getName()
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "신청자 목록 조회 성공",
            response
        ));
    }
}
