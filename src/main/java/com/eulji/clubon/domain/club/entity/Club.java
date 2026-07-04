package com.eulji.clubon.domain.club.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "clubs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubType type;

    @Column(length = 50)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @Column(length = 100)
    private String recruitPeriod;

    @Column(length = 255)
    private String recruitCondition;

    @Column(length = 255)
    private String activityInfo;

    @Column(length = 500)
    private String contactUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubStatus status;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Club(
            String name,
            ClubType type,
            String shortDescription,
            String fullDescription,
            String recruitPeriod,
            String recruitCondition,
            String activityInfo,
            String contactUrl,
            ClubStatus status,
            String imageUrl
    ) {
        this.name = name;
        this.type = type;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.recruitPeriod = recruitPeriod;
        this.recruitCondition = recruitCondition;
        this.activityInfo = activityInfo;
        this.contactUrl = contactUrl;
        this.status = status == null ? ClubStatus.OPEN : status;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (status == null) {
            status = ClubStatus.OPEN;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void updateInfo(
            ClubStatus status,
            String shortDescription,
            String fullDescription,
            String recruitPeriod,
            String recruitCondition,
            String activityInfo,
            String contactUrl,
            String imageUrl
    ) {
        if (status != null) {
            this.status = status;
        }
        if (shortDescription != null) {
            this.shortDescription = shortDescription;
        }
        if (fullDescription != null) {
            this.fullDescription = fullDescription;
        }
        if (recruitPeriod != null) {
            this.recruitPeriod = recruitPeriod;
        }
        if (recruitCondition != null) {
            this.recruitCondition = recruitCondition;
        }
        if (activityInfo != null) {
            this.activityInfo = activityInfo;
        }
        if (contactUrl != null) {
            this.contactUrl = contactUrl;
        }
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }
}
