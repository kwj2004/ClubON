package com.eulji.clubon.domain.notification.repository;

import com.eulji.clubon.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByMember_EmailOrderByCreatedAtDesc(String email, Pageable pageable);

    Optional<Notification> findByIdAndMember_Email(Long notificationId, String email);

    long countByMember_EmailAndReadFalse(String email);

    List<Notification> findByMember_EmailAndReadFalse(String email);
}
