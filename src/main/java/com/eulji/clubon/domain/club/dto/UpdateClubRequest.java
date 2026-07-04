package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubStatus;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateClubRequest(
        ClubStatus status,

        @Size(max = 100, message = "한 줄 소개는 최대 100자까지 입력할 수 있습니다.")
        String shortDescription,

        String fullDescription,

        String recruitPeriod,

        String recruitCondition,

        String activityInfo,

        @URL(message = "문의 링크 형식이 올바르지 않습니다.")
        String contactUrl,

        @URL(message = "썸네일 이미지 URL 형식이 올바르지 않습니다.")
        String imageUrl
) {

    public boolean hasAnyValue() {
        return status != null
                || shortDescription != null
                || fullDescription != null
                || recruitPeriod != null
                || recruitCondition != null
                || activityInfo != null
                || contactUrl != null
                || imageUrl != null;
    }
}
