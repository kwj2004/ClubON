package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ClubCreationRequestDetailResponse;
import com.eulji.clubon.domain.club.dto.ClubCreationRequestListResponse;
import com.eulji.clubon.domain.club.dto.CreateClubRequest;
import com.eulji.clubon.domain.club.dto.CreateClubResponse;
import com.eulji.clubon.domain.club.entity.ClubCreationRequest;
import com.eulji.clubon.domain.club.entity.ClubCreationRequestStatus;
import com.eulji.clubon.domain.club.repository.ClubApplicationQuestionRepository;
import com.eulji.clubon.domain.club.repository.ClubCreationRequestRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.ClubCreationRequestNotFoundException;
import com.eulji.clubon.global.error.DuplicateClubNameException;
import com.eulji.clubon.domain.club.dto.AdminClubCreationRequestListResponse;
import com.eulji.clubon.domain.club.dto.AdminClubCreationRequestPageResponse;
import com.eulji.clubon.domain.club.dto.AdminClubCreationRequestDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eulji.clubon.domain.club.dto.ApproveClubCreationRequestResponse;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubMembership;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.dto.RejectClubCreationRequestRequest;
import com.eulji.clubon.domain.club.dto.RejectClubCreationRequestResponse;
import com.eulji.clubon.domain.club.repository.ClubApplicationQuestionRepository;
import com.eulji.clubon.domain.club.entity.ApplicationQuestionType;
import com.eulji.clubon.domain.club.entity.ClubApplicationQuestion;
import com.eulji.clubon.domain.club.dto.ApplicationQuestionRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubCreationRequestService {

    private final ClubRepository clubRepository;
    private final ClubCreationRequestRepository clubCreationRequestRepository;
    private final MemberRepository memberRepository;
    private final ClubMembershipRepository clubMembershipRepository;
    private final ClubApplicationQuestionRepository clubApplicationQuestionRepository;

    @Transactional
    public CreateClubResponse createRequest(CreateClubRequest request, String email) {
        String normalizedName = request.name().trim();

        if (clubRepository.existsByName(normalizedName)
                || clubCreationRequestRepository.existsByNameAndStatus(normalizedName, ClubCreationRequestStatus.PENDING)) {
            throw new DuplicateClubNameException();
        }

        Member requester = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        ClubCreationRequest creationRequest = clubCreationRequestRepository.save(ClubCreationRequest.builder()
                .requester(requester)
                .name(normalizedName)
                .type(request.type())
                .category(request.category())
                .shortDescription(request.shortDescription().trim())
                .fullDescription(request.fullDescription().trim())
                .build());
        if (request.applicationQuestions() != null) {
            validateQuestions(request.applicationQuestions());

            List<ClubApplicationQuestion> questions =
                request.applicationQuestions()
                    .stream()
                    .map(question -> new ClubApplicationQuestion(
                        creationRequest,
                        question.label().trim(),
                        question.type(),
                        question.required(),
                        question.sortOrder(),
                        normalizeOptions(question)
                    ))
                    .toList();

            clubApplicationQuestionRepository.saveAll(questions);
        }
        return CreateClubResponse.from(creationRequest);
    } //개설 신청 저장 (질문 검증, 질문 저장, 응답 반환)

    public List<ClubCreationRequestListResponse> getMyRequests(String email) {
        return clubCreationRequestRepository.findByRequester_EmailOrderByCreatedAtDesc(email)
                .stream()
                .map(ClubCreationRequestListResponse::from)
                .toList();
    }

    public ClubCreationRequestDetailResponse getMyRequestDetail(Long requestId, String email) {
        return clubCreationRequestRepository.findByIdAndRequester_Email(requestId, email)
                .map(ClubCreationRequestDetailResponse::from)
                .orElseThrow(ClubCreationRequestNotFoundException::new);
    }

    public AdminClubCreationRequestPageResponse getAdminRequests(
        ClubCreationRequestStatus status,
        int page,
        int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<AdminClubCreationRequestListResponse> response = clubCreationRequestRepository
            .findAdminRequests(status, pageRequest)
            .map(AdminClubCreationRequestListResponse::from);

        return AdminClubCreationRequestPageResponse.from(response);
    }
    public AdminClubCreationRequestDetailResponse getAdminRequestDetail(Long requestId) {
        return clubCreationRequestRepository.findAdminRequestDetail(requestId)
            .map(AdminClubCreationRequestDetailResponse::from)
            .orElseThrow(ClubCreationRequestNotFoundException::new);
    }
    @Transactional
    public ApproveClubCreationRequestResponse approveAdminRequest(Long requestId) {
        ClubCreationRequest request = clubCreationRequestRepository.findAdminRequestDetail(requestId)
            .orElseThrow(ClubCreationRequestNotFoundException::new);

        if (clubRepository.existsByName(request.getName())) {
            throw new DuplicateClubNameException();
        }

        Club club = clubRepository.save(Club.builder()
            .name(request.getName())
            .type(request.getType())
            .category(request.getCategory())
            .shortDescription(request.getShortDescription())
            .fullDescription(request.getFullDescription())
            .status(ClubStatus.OPEN)
            .build());

        clubApplicationQuestionRepository
            .findByCreationRequest_IdOrderBySortOrderAsc(requestId)
            .forEach(question -> question.connectClub(club));
        // 승인 시 동아리 연결

        clubMembershipRepository.save(ClubMembership.builder()
            .club(club)
            .member(request.getRequester())
            .role(ClubMemberRole.ADMIN)
            .build());

        request.approve(club);

        return ApproveClubCreationRequestResponse.from(request);
    }
    @Transactional
    public RejectClubCreationRequestResponse rejectAdminRequest(
        Long requestId,
        RejectClubCreationRequestRequest request
    ) {
        ClubCreationRequest creationRequest = clubCreationRequestRepository.findAdminRequestDetail(requestId)
            .orElseThrow(ClubCreationRequestNotFoundException::new);

        creationRequest.reject(request.rejectedReason().trim());

        return RejectClubCreationRequestResponse.from(creationRequest);
    }

    private void validateQuestions(List<ApplicationQuestionRequest> questions) {
        long distinctSortOrders = questions.stream()
            .map(ApplicationQuestionRequest::sortOrder)
            .distinct()
            .count();

        if (distinctSortOrders != questions.size()) {
            throw new IllegalArgumentException("질문 순서는 중복될 수 없습니다.");
        }

        for (ApplicationQuestionRequest question : questions) {
            boolean choiceType =
                question.type() == ApplicationQuestionType.SELECT
                    || question.type() == ApplicationQuestionType.CHECKBOX;

            boolean hasOptions =
                question.options() != null
                    && question.options().stream()
                    .anyMatch(option -> option != null && !option.isBlank());

            if (choiceType && !hasOptions) {
                throw new IllegalArgumentException(
                    "선택형 질문에는 하나 이상의 선택지가 필요합니다."
                );
            }

            if (!choiceType && hasOptions) {
                throw new IllegalArgumentException(
                    "주관식 질문에는 선택지를 등록할 수 없습니다."
                );
            }
        }
    }

    private List<String> normalizeOptions(ApplicationQuestionRequest question) {
        if (question.options() == null) {
            return List.of();
        }

        return question.options().stream()
            .map(String::trim)
            .filter(option -> !option.isBlank())
            .distinct()
            .toList();
    }

}
