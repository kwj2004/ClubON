package com.eulji.clubon.domain.notification.service;

import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.domain.notification.dto.NotificationPageResponse;
import com.eulji.clubon.domain.notification.dto.NotificationResponse;
import com.eulji.clubon.domain.notification.dto.UnreadNotificationCountResponse;
import com.eulji.clubon.domain.notification.entity.Notification;
import com.eulji.clubon.domain.notification.entity.NotificationType;
import com.eulji.clubon.domain.notification.repository.NotificationRepository;
import com.eulji.clubon.global.error.MemberNotFoundException;
import com.eulji.clubon.global.error.NotificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public NotificationPageResponse getMyNotifications(String email, int page, int size) {
        validatePageRequest(page, size);

        return NotificationPageResponse.from(
            notificationRepository.findByMember_EmailOrderByCreatedAtDesc(
                    email,
                    PageRequest.of(page, size)
                )
                .map(NotificationResponse::from)
        );
    }

    public UnreadNotificationCountResponse getUnreadCount(String email) {
        return new UnreadNotificationCountResponse(
            notificationRepository.countByMember_EmailAndReadFalse(email)
        );
    }

    @Transactional
    public void markAsRead(Long notificationId, String email) {
        Notification notification = notificationRepository.findByIdAndMember_Email(notificationId, email)
            .orElseThrow(NotificationNotFoundException::new);

        notification.markAsRead();
    }

    @Transactional
    public void markAllAsRead(String email) {
        notificationRepository.findByMember_EmailAndReadFalse(email)
            .forEach(Notification::markAsRead);
    }

    @Transactional
    public void createNotification(
        Member member,
        NotificationType type,
        String title,
        String content,
        String linkUrl
    ) {
        notificationRepository.save(Notification.builder()
            .member(member)
            .type(type)
            .title(title)
            .content(content)
            .linkUrl(linkUrl)
            .build());
    }

    @Transactional
    public void createNotification(
        String email,
        NotificationType type,
        String title,
        String content,
        String linkUrl
    ) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);

        createNotification(member, type, title, content, linkUrl);
    }

    @Transactional
    public void createNotifications(
        Collection<Member> members,
        NotificationType type,
        String title,
        String content,
        String linkUrl
    ) {
        List<Notification> notifications = members.stream()
            .map(member -> Notification.builder()
                .member(member)
                .type(type)
                .title(title)
                .content(content)
                .linkUrl(linkUrl)
                .build())
            .toList();

        notificationRepository.saveAll(notifications);
    }

    private void validatePageRequest(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("페이지 번호는 0 이상이어야 합니다.");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("페이지 크기는 1 이상 100 이하로 입력해주세요.");
        }
    }
}
