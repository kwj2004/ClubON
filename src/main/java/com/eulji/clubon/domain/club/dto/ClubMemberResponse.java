package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubMembership;

import java.time.LocalDateTime;

public record ClubMemberResponse(
    Long membershipId,
    Long memberId,
    String name,
    String studentId,
    String department,
    String email,
    ClubMemberRole role,
    LocalDateTime joinedAt
) {

    public static ClubMemberResponse from(ClubMembership membership) {
        return new ClubMemberResponse(
            membership.getId(),
            membership.getMember().getId(),
            membership.getMember().getName(),
            membership.getMember().getStudentId(),
            membership.getMember().getDepartment(),
            membership.getMember().getEmail(),
            membership.getRole(),
            membership.getJoinedAt()
        );
    }
}
