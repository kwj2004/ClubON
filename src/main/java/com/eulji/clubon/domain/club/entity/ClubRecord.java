package com.eulji.clubon.domain.club.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "club_records")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(
        name = "club_record_image_urls",
        joinColumns = @JoinColumn(name = "record_id")
    )
    @OrderColumn(name = "image_order")
    @Column(name = "image_url", length = 500)
    private List<String> imageUrls = new ArrayList<>();

    @Column(length = 500)
    private String thumbnailUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public ClubRecord(Club club, String title, String content, List<String> imageUrls) {
        this.club = club;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls == null ? new ArrayList<>() : new ArrayList<>(imageUrls);
        this.thumbnailUrl = this.imageUrls.isEmpty() ? null : this.imageUrls.get(0);
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (thumbnailUrl == null && imageUrls != null && !imageUrls.isEmpty()) {
            thumbnailUrl = imageUrls.get(0);
        }
    }
    public void update(String title, String content, List<String> imageUrls) {
        if (title != null) {
            this.title = title;
        }

        if (content != null) {
            this.content = content;
        }

        if (imageUrls != null) {
            this.imageUrls.clear();
            this.imageUrls.addAll(imageUrls);
            this.thumbnailUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);
        }
    }
}
