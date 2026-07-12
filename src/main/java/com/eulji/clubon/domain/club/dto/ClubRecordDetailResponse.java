package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubRecord;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ClubRecordDetailResponse(
    Long recordId,
    Long clubId,
    String title,
    String content,
    List<String> imageUrls,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt
) {

    public static ClubRecordDetailResponse from(ClubRecord record) {
        return new ClubRecordDetailResponse(
            record.getId(),
            record.getClub().getId(),
            record.getTitle(),
            record.getContent(),
            record.getImageUrls(),
            record.getCreatedAt()
        );
    }
}
