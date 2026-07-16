package com.eulji.clubon.domain.club.controller;

import com.eulji.clubon.domain.club.dto.CreateClubRecordRequest;
import com.eulji.clubon.domain.club.dto.CreateClubRecordResponse;
import com.eulji.clubon.domain.club.service.ClubRecordService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.eulji.clubon.domain.club.dto.ClubRecordListResponse;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import com.eulji.clubon.domain.club.dto.ClubRecordDetailResponse;
import com.eulji.clubon.domain.club.dto.UpdateClubRecordRequest;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.eulji.clubon.domain.club.dto.RecordImageUploadRequest;
import com.eulji.clubon.domain.club.dto.RecordImageUploadResponse;
import com.eulji.clubon.domain.club.service.ClubRecordImageService;

@RestController
@RequiredArgsConstructor
public class ClubRecordController {

    private final ClubRecordService clubRecordService;
    private final ClubRecordImageService clubRecordImageService;

    @PostMapping("/api/clubs/{clubId}/record-images/presigned-url")
    public ResponseEntity<ApiResponse<RecordImageUploadResponse>> createRecordImageUploadUrl(
        @PathVariable Long clubId,
        Authentication authentication,
        @Valid @RequestBody RecordImageUploadRequest request
    ) {
        RecordImageUploadResponse response = clubRecordImageService.createUploadUrl(
            clubId, authentication.getName(), request
        );
        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(), "활동 기록 이미지 업로드 URL이 발급되었습니다.", response
        ));
    }

    @PostMapping("/api/clubs/{clubId}/records")
    public ResponseEntity<ApiResponse<CreateClubRecordResponse>> createRecord(
        @PathVariable Long clubId,
        Authentication authentication,
        @Valid @RequestBody CreateClubRecordRequest request
    ) {
        CreateClubRecordResponse response = clubRecordService.createRecord(
            clubId,
            authentication.getName(),
            request
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.of(
                HttpStatus.CREATED.value(),
                "활동 기록이 성공적으로 작성되었습니다.",
                response
            ));
    }
    @GetMapping("/api/clubs/{clubId}/records")
    public ResponseEntity<ApiResponse<List<ClubRecordListResponse>>> getRecords(
        @PathVariable Long clubId
    ) {
        List<ClubRecordListResponse> response = clubRecordService.getRecords(clubId);

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "활동 기록 목록 조회 성공",
            response
        ));
    }
    @GetMapping("/api/clubs/{clubId}/records/{recordId}")
    public ResponseEntity<ApiResponse<ClubRecordDetailResponse>> getRecordDetail(
        @PathVariable Long clubId,
        @PathVariable Long recordId
    ) {
        ClubRecordDetailResponse response = clubRecordService.getRecordDetail(clubId, recordId);

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "활동 기록 상세 조회 성공",
            response
        ));
    }
    @PatchMapping("/api/clubs/{clubId}/records/{recordId}")
    public ResponseEntity<ApiResponse<Void>> updateRecord(
        @PathVariable Long clubId,
        @PathVariable Long recordId,
        Authentication authentication,
        @Valid @RequestBody UpdateClubRecordRequest request
    ) {
        clubRecordService.updateRecord(
            clubId,
            recordId,
            authentication.getName(),
            request
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "활동 기록이 성공적으로 수정되었습니다.",
            null
        ));
    }
    @DeleteMapping("/api/clubs/{clubId}/records/{recordId}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(
        @PathVariable Long clubId,
        @PathVariable Long recordId,
        Authentication authentication
    ) {
        clubRecordService.deleteRecord(
            clubId,
            recordId,
            authentication.getName()
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "활동 기록이 삭제되었습니다.",
            null
        ));
    }
}
