package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ApplicationQuestionType;
import com.eulji.clubon.domain.club.entity.ClubApplicationQuestion;

import java.util.List;

public record ApplicationQuestionResponse(
    Long questionId,
    String label,
    ApplicationQuestionType type,
    boolean required,
    int sortOrder,
    List<String> options
) {
    public static ApplicationQuestionResponse from(
        ClubApplicationQuestion question
    ) {
        return new ApplicationQuestionResponse(
            question.getId(),
            question.getLabel(),
            question.getType(),
            question.isRequired(),
            question.getSortOrder(),
            List.copyOf(question.getOptions())
        );
    }
}
