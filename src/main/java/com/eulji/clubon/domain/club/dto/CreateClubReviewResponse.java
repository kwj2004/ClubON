package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubReview;

public record CreateClubReviewResponse(
    Long reviewId
) {

    public static CreateClubReviewResponse from(ClubReview review) {
        return new CreateClubReviewResponse(review.getId());
    }
}
