package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubPost;
import com.eulji.clubon.domain.club.entity.ClubPostCategory;
import com.eulji.clubon.domain.club.entity.ClubPostStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ClubPostDetailResponse(
    Long postId,
    Long clubId,
    ClubPostCategory category,
    ClubPostStatus status,
    String title,
    String content,
    List<String> attachmentUrls,
    Long authorId,
    String authorName,
    Long viewCount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ClubPostDetailResponse from(ClubPost post) {
        return new ClubPostDetailResponse(
            post.getId(),
            post.getClub().getId(),
            post.getCategory(),
            post.getStatus(),
            post.getTitle(),
            post.getContent(),
            List.copyOf(post.getAttachmentUrls()),
            post.getAuthor().getId(),
            post.getAuthor().getName(),
            post.getViewCount(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}
