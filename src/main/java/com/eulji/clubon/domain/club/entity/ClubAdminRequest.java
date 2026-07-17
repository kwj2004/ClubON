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
@Table(name = "club_admin_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 회원가입 중 동아리 운영자로 신청한 내역을 저장하는 엔티티입니다.
public class ClubAdminRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(nullable = false, length = 30)
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubAdminRequestStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;

    @Column(length = 500)
    private String rejectedReason;

    @Builder
    public ClubAdminRequest(Member member, Club club, String position) {
        this.member = member;
        this.club = club;
        this.position = position;
        this.status = ClubAdminRequestStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (status == null) {
            status = ClubAdminRequestStatus.PENDING;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void approve() {
        validatePending();
        this.status = ClubAdminRequestStatus.APPROVED;
        this.reviewedAt = LocalDateTime.now();
        this.rejectedReason = null;
    }

    public void reject(String reason) {
        validatePending();
        this.status = ClubAdminRequestStatus.REJECTED;
        this.reviewedAt = LocalDateTime.now();
        this.rejectedReason = reason;
    }

    private void validatePending() {
        if (status != ClubAdminRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 운영자 신청입니다.");
        }
    }
}
