package com.eulji.clubon.domain.inquiry.controller;

import com.eulji.clubon.domain.inquiry.dto.AdminInquiryPageResponse;
import com.eulji.clubon.domain.inquiry.dto.AnswerInquiryRequest;
import com.eulji.clubon.domain.inquiry.dto.AnswerInquiryResponse;
import com.eulji.clubon.domain.inquiry.entity.InquiryStatus;
import com.eulji.clubon.domain.inquiry.entity.InquiryType;
import com.eulji.clubon.domain.inquiry.service.InquiryService;
import com.eulji.clubon.global.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/admin/inquiries")
@RequiredArgsConstructor
public class AdminInquiryController {

    private final InquiryService inquiryService;

    @GetMapping
    public ResponseEntity<ApiResponse<AdminInquiryPageResponse>> getInquiries(
        @RequestParam(required = false) InquiryStatus status,
        @RequestParam(required = false) InquiryType type,
        @RequestParam(defaultValue = "0")
        @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
        int page,
        @RequestParam(defaultValue = "10")
        @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
        @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
        int size
    ) {
        AdminInquiryPageResponse response = inquiryService.getAdminInquiries(
            status,
            type,
            page,
            size
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "전체 문의 목록 조회에 성공했습니다.",
            response
        ));
    }

    @PatchMapping("/{inquiryId}/answer")
    public ResponseEntity<ApiResponse<AnswerInquiryResponse>> answerInquiry(
        @PathVariable Long inquiryId,
        @Valid @RequestBody AnswerInquiryRequest request
    ) {
        AnswerInquiryResponse response = inquiryService.answerInquiry(
            inquiryId,
            request
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "문의 답변이 등록되었습니다.",
            response
        ));
    }
}
