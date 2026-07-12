package com.eulji.clubon.domain.notification.dto;

import com.eulji.clubon.domain.notification.entity.Notification;
import com.eulji.clubon.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long notificationId,
    NotificationType type,
    String title,
    String content,
    String linkUrl,
    boolean read,
    LocalDateTime createdAt,
    LocalDateTime readAt
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getType(),
            notification.getTitle(),
            notification.getContent(),
            notification.getLinkUrl(),
            notification.isRead(),
            notification.getCreatedAt(),
            notification.getReadAt()
        );
    }
}
