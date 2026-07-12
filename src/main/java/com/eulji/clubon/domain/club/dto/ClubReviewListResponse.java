package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubReview;

import java.time.LocalDateTime;

public record ClubReviewListResponse(
    Long reviewId,
    Integer rating,
    String content,
    String writerName,
    String writerDepartment,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ClubReviewListResponse from(ClubReview review) {
        return new ClubReviewListResponse(
            review.getId(),
            review.getRating(),
            review.getContent(),
            review.getMember().getName(),
            review.getMember().getDepartment(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }
}
