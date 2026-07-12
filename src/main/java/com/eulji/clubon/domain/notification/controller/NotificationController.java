package com.eulji.clubon.domain.notification.controller;

import com.eulji.clubon.domain.notification.dto.NotificationPageResponse;
import com.eulji.clubon.domain.notification.dto.UnreadNotificationCountResponse;
import com.eulji.clubon.domain.notification.service.NotificationService;
import com.eulji.clubon.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<NotificationPageResponse>> getMyNotifications(
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        NotificationPageResponse response = notificationService.getMyNotifications(
            authentication.getName(),
            page,
            size
        );

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "내 알림 목록 조회 성공",
            response
        ));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<UnreadNotificationCountResponse>> getUnreadCount(
        Authentication authentication
    ) {
        UnreadNotificationCountResponse response =
            notificationService.getUnreadCount(authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "읽지 않은 알림 수 조회 성공",
            response
        ));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
        @PathVariable Long notificationId,
        Authentication authentication
    ) {
        notificationService.markAsRead(notificationId, authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "알림이 읽음 처리되었습니다.",
            null
        ));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(Authentication authentication) {
        notificationService.markAllAsRead(authentication.getName());

        return ResponseEntity.ok(ApiResponse.of(
            HttpStatus.OK.value(),
            "모든 알림이 읽음 처리되었습니다.",
            null
        ));
    }
}
