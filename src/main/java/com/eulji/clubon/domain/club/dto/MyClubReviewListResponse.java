package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubReview;

import java.time.LocalDateTime;

public record MyClubReviewListResponse(
    Long reviewId,
    Long clubId,
    String clubName,
    Integer rating,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static MyClubReviewListResponse from(ClubReview review) {
        return new MyClubReviewListResponse(
            review.getId(),
            review.getClub().getId(),
            review.getClub().getName(),
            review.getRating(),
            review.getContent(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }
}
