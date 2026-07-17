package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.ApplicationApplicantInfoResponse;
import com.eulji.clubon.domain.club.dto.ApplicationQuestionResponse;
import com.eulji.clubon.domain.club.dto.ClubApplicationFormResponse;
import com.eulji.clubon.domain.club.repository.ClubApplicationQuestionRepository;
import com.eulji.clubon.domain.club.dto.MyClubApplicationResponse;
import com.eulji.clubon.domain.club.dto.ClubApplicationApplicantResponse;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubMembership;
import com.eulji.clubon.domain.club.dto.UpdateClubApplicationStatusRequest;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import com.eulji.clubon.domain.club.dto.CreateClubApplicationRequest;
import com.eulji.clubon.domain.club.dto.CreateClubApplicationResponse;
import com.eulji.clubon.domain.club.entity.ClubActivityLogType;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubApplication;
import com.eulji.clubon.domain.club.entity.ClubApplicationStatus;
import com.eulji.clubon.domain.club.repository.ClubApplicationRepository;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.domain.notification.entity.NotificationType;
import com.eulji.clubon.domain.notification.service.NotificationService;
import com.eulji.clubon.global.error.AlreadyClubMemberException;
import com.eulji.clubon.global.error.ClubNotFoundException;
import com.eulji.clubon.global.error.DuplicateClubApplicationException;
import com.eulji.clubon.global.error.MemberNotFoundException;
import com.eulji.clubon.global.error.ClubApplicationNotFoundException;
import com.eulji.clubon.global.error.ClubRecruitmentClosedException;
import com.eulji.clubon.domain.club.util.RecruitmentStatusResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eulji.clubon.domain.club.dto.ApplicationAnswerRequest;
import com.eulji.clubon.domain.club.entity.ApplicationQuestionType;
import com.eulji.clubon.domain.club.entity.ClubApplicationAnswer;
import com.eulji.clubon.domain.club.entity.ClubApplicationQuestion;
import com.eulji.clubon.domain.club.repository.ClubApplicationAnswerRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubApplicationService {

    private final ClubApplicationRepository clubApplicationRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubMembershipRepository clubMembershipRepository;
    private final ClubApplicationQuestionRepository clubApplicationQuestionRepository;
    private final ClubApplicationAnswerRepository clubApplicationAnswerRepository;
    private final NotificationService notificationService;
    private final ClubActivityLogService clubActivityLogService;

    @Transactional
    public ClubApplicationFormResponse getApplicationForm(
        Long clubId,
        String email
    ) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(ClubNotFoundException::new);

        validateRecruiting(club);

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);

        List<ApplicationQuestionResponse> questions =
            clubApplicationQuestionRepository
                .findByClub_IdOrderBySortOrderAsc(clubId)
                .stream()
                .map(ApplicationQuestionResponse::from)
                .toList();

        return new ClubApplicationFormResponse(
            club.getId(),
            club.getName(),
            ApplicationApplicantInfoResponse.from(member),
            questions
        );
    }

    @Transactional
    public CreateClubApplicationResponse createApplication(
        Long clubId,
        String email,
        CreateClubApplicationRequest request
    ) {
        Club club = clubRepository.findById(clubId)
            .orElseThrow(ClubNotFoundException::new);

        validateRecruiting(club);

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);

        if (clubMembershipRepository
            .existsByClub_IdAndMember_Email(clubId, email)) {
            throw new AlreadyClubMemberException();
        }

        boolean hasPendingApplication =
            clubApplicationRepository
                .existsByClub_IdAndMember_EmailAndStatus(
                    clubId,
                    email,
                    ClubApplicationStatus.PENDING
                );

        if (hasPendingApplication) {
            throw new DuplicateClubApplicationException();
        }

        List<ClubApplicationQuestion> questions =
            clubApplicationQuestionRepository
                .findByClub_IdOrderBySortOrderAsc(clubId);

        Map<Long, ClubApplicationQuestion> questionMap = new HashMap<>();

        for (ClubApplicationQuestion question : questions) {
            questionMap.put(question.getId(), question);
        }

        validateAnswers(
            request.answers(),
            questions,
            questionMap
        );

        ClubApplication application =
            clubApplicationRepository.save(
                ClubApplication.builder()
                    .club(club)
                    .member(member)
                    .content(null)
                    .build()
            );

        List<ClubApplicationAnswer> answers = request.answers()
            .stream()
            .map(answer -> {
                ClubApplicationQuestion question =
                    questionMap.get(answer.questionId());

                return new ClubApplicationAnswer(
                    application,
                    question,
                    normalizeAnswerValues(answer)
                );
            })
            .filter(answer -> !answer.getValues().isEmpty())
            .toList();

        clubApplicationAnswerRepository.saveAll(answers);

        notificationService.createNotifications(
            clubMembershipRepository.findMembersByClubIdAndRole(clubId, ClubMemberRole.ADMIN),
            NotificationType.CLUB_APPLICATION,
            "새로운 가입 신청이 도착했습니다.",
            member.getName() + "님이 " + club.getName() + "에 가입 신청했습니다.",
            "/clubs/" + clubId + "/applications"
        );

        clubActivityLogService.log(
            clubId,
            email,
            ClubActivityLogType.APPLICATION_CREATED,
            member.getName() + "님의 가입 신청이 등록되었습니다.",
            "/clubs/" + clubId + "/applications"
        );

        return CreateClubApplicationResponse.from(application);
    }
    public List<MyClubApplicationResponse> getMyApplications(String email) {
        return clubApplicationRepository.findMyApplications(email)
            .stream()
            .map(MyClubApplicationResponse::from)
            .toList();
    }
    @Transactional
    public void cancelApplication(Long applicationId, String email) {
        ClubApplication application = clubApplicationRepository.findByIdAndMember_Email(applicationId, email)
            .orElseThrow(ClubApplicationNotFoundException::new);

        application.cancel();
    }

    public List<ClubApplicationApplicantResponse> getClubApplications(
        Long clubId,
        ClubApplicationStatus status,
        String email
    ) {
        if (!clubRepository.existsById(clubId)) {
            throw new ClubNotFoundException();
        }

        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("해당 동아리의 가입 신청 목록을 조회할 권한이 없습니다.");
        }

        return clubApplicationRepository.findClubApplications(clubId, status)
            .stream()
            .map(ClubApplicationApplicantResponse::from)
            .toList();
    }
    @Transactional
    public ClubApplicationStatus updateApplicationStatus(
        Long applicationId,
        String email,
        UpdateClubApplicationStatusRequest request
    ) {
        ClubApplication application = clubApplicationRepository.findByIdWithClubAndMember(applicationId)
            .orElseThrow(ClubApplicationNotFoundException::new);

        Long clubId = application.getClub().getId();

        boolean isClubAdmin = clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(
            clubId,
            email,
            ClubMemberRole.ADMIN
        );

        if (!isClubAdmin) {
            throw new AccessDeniedException("해당 동아리의 가입 신청을 처리할 권한이 없습니다.");
        }

        if (request.status() == ClubApplicationStatus.APPROVED) {
            application.approve();

            if (!clubMembershipRepository.existsByClub_IdAndMember_Email(
                clubId,
                application.getMember().getEmail()
            )) {
                clubMembershipRepository.save(ClubMembership.builder()
                    .club(application.getClub())
                    .member(application.getMember())
                    .role(ClubMemberRole.MEMBER)
                    .build());
            }

            notificationService.createNotification(
                application.getMember(),
                NotificationType.CLUB_APPLICATION,
                "가입 신청이 승인되었습니다.",
                application.getClub().getName() + " 가입 신청이 승인되었습니다.",
                "/clubs/" + clubId
            );

            clubActivityLogService.log(
                clubId,
                email,
                ClubActivityLogType.APPLICATION_APPROVED,
                application.getMember().getName() + "님의 가입 신청을 승인했습니다.",
                "/clubs/" + clubId + "/applications"
            );

            return application.getStatus();
        }

        if (request.status() == ClubApplicationStatus.REJECTED) {
            application.reject();
            notificationService.createNotification(
                application.getMember(),
                NotificationType.CLUB_APPLICATION,
                "가입 신청이 거절되었습니다.",
                application.getClub().getName() + " 가입 신청이 거절되었습니다.",
                "/users/me/applications"
            );
            clubActivityLogService.log(
                clubId,
                email,
                ClubActivityLogType.APPLICATION_REJECTED,
                application.getMember().getName() + "님의 가입 신청을 거절했습니다.",
                "/clubs/" + clubId + "/applications"
            );
            return application.getStatus();
        }

        throw new IllegalArgumentException("가입 신청 처리는 APPROVED 또는 REJECTED만 가능합니다.");
    }
    private void validateRecruiting(Club club) {
        if (!RecruitmentStatusResolver.resolve(club.getStatus(), club.getRecruitPeriod()).isRecruiting()) {
            throw new ClubRecruitmentClosedException();
        }
    }

    private void validateAnswers(
        List<ApplicationAnswerRequest> answers,
        List<ClubApplicationQuestion> questions,
        Map<Long, ClubApplicationQuestion> questionMap
    ) {
        Map<Long, ApplicationAnswerRequest> answerMap = new HashMap<>();

        for (ApplicationAnswerRequest answer : answers) {
            if (answerMap.put(answer.questionId(), answer) != null) {
                throw new IllegalArgumentException(
                    "같은 질문에 대한 답변을 중복해서 제출할 수 없습니다."
                );
            }

            if (!questionMap.containsKey(answer.questionId())) {
                throw new IllegalArgumentException(
                    "해당 동아리의 지원서 질문이 아닙니다."
                );
            }
        }

        for (ClubApplicationQuestion question : questions) {
            ApplicationAnswerRequest answer = answerMap.get(question.getId());

            List<String> values = answer == null
                ? List.of()
                : normalizeAnswerValues(answer);

            if (question.isRequired() && values.isEmpty()) {
                throw new IllegalArgumentException(
                    "'" + question.getLabel() + "' 질문은 필수입니다."
                );
            }

            if (values.isEmpty()) {
                continue;
            }

            validateAnswerType(question, values);
        }
    }
    private void validateAnswerType(
        ClubApplicationQuestion question,
        List<String> values
    ) {
        ApplicationQuestionType type = question.getType();

        if ((type == ApplicationQuestionType.TEXT
            || type == ApplicationQuestionType.TEXTAREA)
            && values.size() != 1) {
            throw new IllegalArgumentException(
                "주관식 질문은 하나의 답변만 입력할 수 있습니다."
            );
        }

        if (type == ApplicationQuestionType.SELECT
            && values.size() != 1) {
            throw new IllegalArgumentException(
                "단일 선택 질문은 하나만 선택할 수 있습니다."
            );
        }

        if (type == ApplicationQuestionType.SELECT
            || type == ApplicationQuestionType.CHECKBOX) {

            boolean invalidOption = values.stream()
                .anyMatch(value -> !question.getOptions().contains(value));

            if (invalidOption) {
                throw new IllegalArgumentException(
                    "등록되지 않은 선택지가 포함되어 있습니다."
                );
            }
        }
    }
    private List<String> normalizeAnswerValues(
        ApplicationAnswerRequest answer
    ) {
        if (answer.values() == null) {
            return List.of();
        }

        return answer.values()
            .stream()
            .filter(value -> value != null && !value.isBlank())
            .map(String::trim)
            .distinct()
            .toList();
    }
}
