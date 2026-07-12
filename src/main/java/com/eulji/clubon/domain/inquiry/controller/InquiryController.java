package com.eulji.clubon.domain.inquiry.controller;

import com.eulji.clubon.domain.inquiry.dto.CreateInquiryRequest;
import com.eulji.clubon.domain.inquiry.dto.CreateInquiryResponse;
import com.eulji.clubon.domain.inquiry.dto.MyInquiryDetailResponse;
import com.eulji.clubon.domain.inquiry.dto.MyInquiryListResponse;
import com.eulji.clubon.domain.inquiry.service.InquiryService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateInquiryResponse>> createInquiry(
        Authentication authentication,
        @Valid @RequestBody CreateInquiryRequest request
    ) {
        CreateInquiryResponse response = inquiryService.createInquiry(
            authentication.getName(),
            request
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.of(
                HttpStatus.CREATED.value(),
                "문의가 정상적으로 등록되었습니다.",
                response
            ));
    }
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<MyInquiryListResponse>>>
    getMyInquiries(Authentication authentication) {

        List<MyInquiryListResponse> response =
            inquiryService.getMyInquiries(authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "내 문의 목록 조회에 성공했습니다.",
            response
        ));
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse<MyInquiryDetailResponse>> getMyInquiry(
        @PathVariable Long inquiryId,
        Authentication authentication
    ) {
        MyInquiryDetailResponse response = inquiryService.getMyInquiry(
            inquiryId,
            authentication.getName()
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "내 문의 상세 조회에 성공했습니다.",
            response
        ));
    }
}
