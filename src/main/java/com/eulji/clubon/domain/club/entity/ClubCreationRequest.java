package com.eulji.clubon.domain.club.entity;

import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.global.error.AlreadyProcessedClubCreationRequestException;
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

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ClubCategory category;

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
            ClubCategory category,
            String shortDescription,
            String fullDescription
    ) {
        this.requester = requester;
        this.name = name;
        this.type = type;
        this.category = category == null ? ClubCategory.ETC : category;
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
        if (category == null) {
            category = ClubCategory.ETC;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void approve(Club createdClub) {
        if (this.status != ClubCreationRequestStatus.PENDING) {
            throw new AlreadyProcessedClubCreationRequestException();
        }

        this.status = ClubCreationRequestStatus.APPROVED;
        this.createdClub = createdClub;
        this.reviewedAt = LocalDateTime.now();
    }

    public void reject(String rejectedReason) {
        if (this.status != ClubCreationRequestStatus.PENDING) {
            throw new AlreadyProcessedClubCreationRequestException();
        }

        this.status = ClubCreationRequestStatus.REJECTED;
        this.rejectedReason = rejectedReason;
        this.reviewedAt = LocalDateTime.now();
    }

    public ClubCategory getCategory() {
        return category == null ? ClubCategory.ETC : category;
    }
}
