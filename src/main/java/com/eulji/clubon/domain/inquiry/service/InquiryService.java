package com.eulji.clubon.domain.inquiry.service;

import com.eulji.clubon.domain.inquiry.dto.CreateInquiryRequest;
import com.eulji.clubon.domain.inquiry.dto.CreateInquiryResponse;
import com.eulji.clubon.domain.inquiry.dto.AdminInquiryListResponse;
import com.eulji.clubon.domain.inquiry.dto.AdminInquiryPageResponse;
import com.eulji.clubon.domain.inquiry.dto.AnswerInquiryRequest;
import com.eulji.clubon.domain.inquiry.dto.AnswerInquiryResponse;
import com.eulji.clubon.domain.inquiry.dto.MyInquiryDetailResponse;
import com.eulji.clubon.domain.inquiry.dto.MyInquiryListResponse;
import com.eulji.clubon.domain.inquiry.entity.Inquiry;
import com.eulji.clubon.domain.inquiry.entity.InquiryStatus;
import com.eulji.clubon.domain.inquiry.entity.InquiryType;
import com.eulji.clubon.domain.inquiry.repository.InquiryRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.InquiryNotFoundException;
import com.eulji.clubon.global.error.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateInquiryResponse createInquiry(
        String email,
        CreateInquiryRequest request
    ) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);

        Inquiry inquiry = inquiryRepository.save(new Inquiry(
            member,
            request.type(),
            request.title().trim(),
            request.content().trim(),
            trimToNull(request.attachmentUrl())
        ));

        return CreateInquiryResponse.from(inquiry);
    }
    public List<MyInquiryListResponse> getMyInquiries(String email) {
        return inquiryRepository
            .findByMember_EmailOrderByCreatedAtDesc(email)
            .stream()
            .map(MyInquiryListResponse::from)
            .toList();
    }

    public MyInquiryDetailResponse getMyInquiry(Long inquiryId, String email) {
        Inquiry inquiry = inquiryRepository
            .findByIdAndMember_Email(inquiryId, email)
            .orElseThrow(InquiryNotFoundException::new);

        return MyInquiryDetailResponse.from(inquiry);
    }

    public AdminInquiryPageResponse getAdminInquiries(
        InquiryStatus status,
        InquiryType type,
        int page,
        int size
    ) {
        Page<AdminInquiryListResponse> inquiries = inquiryRepository
            .findAdminInquiries(status, type, PageRequest.of(page, size))
            .map(AdminInquiryListResponse::from);

        return AdminInquiryPageResponse.from(inquiries);
    }

    @Transactional
    public AnswerInquiryResponse answerInquiry(
        Long inquiryId,
        AnswerInquiryRequest request
    ) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
            .orElseThrow(InquiryNotFoundException::new);

        inquiry.answer(request.answer().trim());

        return AnswerInquiryResponse.from(inquiry);
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

}
