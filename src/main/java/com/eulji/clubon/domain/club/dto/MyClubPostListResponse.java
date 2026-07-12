package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubPost;
import com.eulji.clubon.domain.club.entity.ClubPostCategory;
import com.eulji.clubon.domain.club.entity.ClubPostStatus;

import java.time.LocalDateTime;

public record MyClubPostListResponse(
    Long postId,
    Long clubId,
    String clubName,
    ClubPostCategory category,
    ClubPostStatus status,
    String title,
    LocalDateTime createdAt,
    Long viewCount
) {

    public static MyClubPostListResponse from(ClubPost post) {
        return new MyClubPostListResponse(
            post.getId(),
            post.getClub().getId(),
            post.getClub().getName(),
            post.getCategory(),
            post.getStatus(),
            post.getTitle(),
            post.getCreatedAt(),
            post.getViewCount()
        );
    }
}
