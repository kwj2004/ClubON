package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubRecord;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ClubRecordListResponse(
    Long recordId,
    String title,
    String thumbnailUrl,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt
) {

    public static ClubRecordListResponse from(ClubRecord record) {
        return new ClubRecordListResponse(
            record.getId(),
            record.getTitle(),
            record.getThumbnailUrl(),
            record.getCreatedAt()
        );
    }

    public static ClubRecordListResponse from(ClubRecord record, String thumbnailUrl) {
        return new ClubRecordListResponse(record.getId(), record.getTitle(), thumbnailUrl, record.getCreatedAt());
    }
}
