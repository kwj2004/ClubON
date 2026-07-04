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
@Table(name = "club_creation_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubCreationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private Member requester;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubType type;

    @Column(nullable = false, length = 100)
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fullDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubCreationRequestStatus status;

    @Column(length = 500)
    private String rejectedReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_club_id")
    private Club createdClub;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;

    @Builder
    public ClubCreationRequest(
            Member requester,
            String name,
            ClubType type,
            String shortDescription,
            String fullDescription
    ) {
        this.requester = requester;
        this.name = name;
        this.type = type;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.status = ClubCreationRequestStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (status == null) {
            status = ClubCreationRequestStatus.PENDING;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
