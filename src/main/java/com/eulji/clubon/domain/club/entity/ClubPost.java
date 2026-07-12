package com.eulji.clubon.domain.club.entity;

import com.eulji.clubon.domain.member.entity.Member;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "club_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubPostCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubPostStatus status;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @CollectionTable(
        name = "club_post_attachment_urls",
        joinColumns = @JoinColumn(name = "post_id")
    )
    @OrderColumn(name = "attachment_order")
    @Column(name = "attachment_url", length = 500)
    private List<String> attachmentUrls = new ArrayList<>();

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public ClubPost(
        Club club,
        Member author,
        ClubPostCategory category,
        ClubPostStatus status,
        String title,
        String content,
        List<String> attachmentUrls
    ) {
        this.club = club;
        this.author = author;
        this.category = category;
        this.status = status == null ? ClubPostStatus.PUBLISHED : status;
        this.title = title;
        this.content = content;
        this.attachmentUrls = attachmentUrls == null ? new ArrayList<>() : new ArrayList<>(attachmentUrls);
        this.viewCount = 0L;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PrePersist
    void prePersist() {
        if (status == null) {
            status = ClubPostStatus.PUBLISHED;
        }
        if (viewCount == null) {
            viewCount = 0L;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    public void increaseViewCount() {
        viewCount++;
    }

    public boolean isAuthor(String email) {
        return author.getEmail().equals(email);
    }

    public void update(
        ClubPostCategory category,
        ClubPostStatus status,
        String title,
        String content,
        List<String> attachmentUrls
    ) {
        if (category != null) {
            this.category = category;
        }
        if (status != null) {
            this.status = status;
        }
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        if (attachmentUrls != null) {
            this.attachmentUrls.clear();
            this.attachmentUrls.addAll(attachmentUrls);
        }
        this.updatedAt = LocalDateTime.now();
    }
}
