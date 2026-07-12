package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubPost;
import com.eulji.clubon.domain.club.entity.ClubPostCategory;

import java.time.LocalDateTime;

public record ClubPostListResponse(
    Long postId,
    ClubPostCategory category,
    String title,
    String authorName,
    LocalDateTime createdAt,
    Long viewCount
) {

    public static ClubPostListResponse from(ClubPost post) {
        return new ClubPostListResponse(
            post.getId(),
            post.getCategory(),
            post.getTitle(),
            post.getAuthor().getName(),
            post.getCreatedAt(),
            post.getViewCount()
        );
    }
}
