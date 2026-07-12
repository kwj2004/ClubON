package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubPost;
import com.eulji.clubon.domain.club.entity.ClubPostStatus;

public record CreateClubPostResponse(
    Long postId,
    ClubPostStatus status
) {

    public static CreateClubPostResponse from(ClubPost post) {
        return new CreateClubPostResponse(
            post.getId(),
            post.getStatus()
        );
    }
}
