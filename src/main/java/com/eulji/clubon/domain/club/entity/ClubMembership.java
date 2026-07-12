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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "club_memberships",
        uniqueConstraints = @UniqueConstraint(columnNames = {"club_id", "member_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClubMemberRole role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Builder
    public ClubMembership(Club club, Member member, ClubMemberRole role) {
        this.club = club;
        this.member = member;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    public void updateRole(ClubMemberRole role) {
        this.role = role;
    }
}
