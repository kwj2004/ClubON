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
@Table(name = "club_applications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubApplicationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @Builder
    public ClubApplication(Club club, Member member, String content) {
        this.club = club;
        this.member = member;
        this.content = content;
        this.status = ClubApplicationStatus.PENDING;
        this.appliedAt = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (status == null) {
            status = ClubApplicationStatus.PENDING;
        }
        if (appliedAt == null) {
            appliedAt = LocalDateTime.now();
        }
    }
    public void cancel() {
        if (this.status != ClubApplicationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 가입 신청만 취소할 수 있습니다.");
        }

        this.status = ClubApplicationStatus.CANCELED;
    }
    public void approve() {
        if (this.status != ClubApplicationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 가입 신청만 승인할 수 있습니다.");
        }

        this.status = ClubApplicationStatus.APPROVED;
    }

    public void reject() {
        if (this.status != ClubApplicationStatus.PENDING) {
            throw new IllegalStateException("대기 중인 가입 신청만 거절할 수 있습니다.");
        }

        this.status = ClubApplicationStatus.REJECTED;
    }
}
