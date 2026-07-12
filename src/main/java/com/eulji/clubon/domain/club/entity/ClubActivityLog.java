package com.eulji.clubon.domain.club.entity;

import com.eulji.clubon.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "club_activity_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private Member actor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ClubActivityLogType type;

    @Column(nullable = false, length = 255)
    private String message;

    @Column(length = 500)
    private String linkUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ClubActivityLog(
        Club club,
        Member actor,
        ClubActivityLogType type,
        String message,
        String linkUrl
    ) {
        this.club = club;
        this.actor = actor;
        this.type = type;
        this.message = message;
        this.linkUrl = linkUrl;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
