package com.eulji.clubon.domain.club.dto;

import java.util.List;

public record ClubApplicationFormResponse(
    Long clubId,
    String clubName,
    ApplicationApplicantInfoResponse applicant,
    List<ApplicationQuestionResponse> questions
) {
}
