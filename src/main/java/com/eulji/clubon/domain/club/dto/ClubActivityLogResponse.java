package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubActivityLog;
import com.eulji.clubon.domain.club.entity.ClubActivityLogType;

import java.time.LocalDateTime;

public record ClubActivityLogResponse(
    Long activityLogId,
    ClubActivityLogType type,
    String message,
    String linkUrl,
    Long actorId,
    String actorName,
    LocalDateTime createdAt
) {

    public static ClubActivityLogResponse from(ClubActivityLog log) {
        return new ClubActivityLogResponse(
            log.getId(),
            log.getType(),
            log.getMessage(),
            log.getLinkUrl(),
            log.getActor() == null ? null : log.getActor().getId(),
            log.getActor() == null ? null : log.getActor().getName(),
            log.getCreatedAt()
        );
    }
}
