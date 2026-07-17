package com.eulji.clubon.domain.club.repository;

import com.eulji.clubon.domain.club.entity.ClubMembership;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubMembershipRepository extends JpaRepository<ClubMembership, Long> {

    @Query("""
            select cm
            from ClubMembership cm
            join fetch cm.club
            join cm.member m
            where m.email = :email
            order by cm.joinedAt desc
            """)
    List<ClubMembership> findMyClubMemberships(@Param("email") String email);

    boolean existsByClub_IdAndMember_EmailAndRole(Long clubId, String email, ClubMemberRole role);
    boolean existsByClub_IdAndMember_Email(Long clubId, String email);
    long countByClub_Id(Long clubId);
    long countByClub_IdAndRole(Long clubId, ClubMemberRole role);
    List<ClubMembership> findAllByMemberAndRole(Member member, ClubMemberRole role);
    void deleteAllByMember(Member member);

    @Query("""
            select cm
            from ClubMembership cm
            join fetch cm.member
            where cm.club.id = :clubId
              and (
                    :keyword is null
                    or lower(cm.member.name) like lower(concat('%', :keyword, '%'))
                    or lower(cm.member.studentId) like lower(concat('%', :keyword, '%'))
                    or lower(cm.member.department) like lower(concat('%', :keyword, '%'))
                    or lower(cm.member.email) like lower(concat('%', :keyword, '%'))
                  )
            order by
              case when cm.role = com.eulji.clubon.domain.club.entity.ClubMemberRole.ADMIN then 0 else 1 end,
              cm.joinedAt desc
            """)
    List<ClubMembership> findClubMembers(
        @Param("clubId") Long clubId,
        @Param("keyword") String keyword
    );

    @Query("""
            select cm
            from ClubMembership cm
            join fetch cm.club
            join fetch cm.member
            where cm.id = :membershipId
              and cm.club.id = :clubId
            """)
    Optional<ClubMembership> findByIdAndClubId(
        @Param("clubId") Long clubId,
        @Param("membershipId") Long membershipId
    );

    @Query("""
            select cm.member
            from ClubMembership cm
            where cm.club.id = :clubId
              and cm.role = :role
            """)
    List<Member> findMembersByClubIdAndRole(
        @Param("clubId") Long clubId,
        @Param("role") ClubMemberRole role
    );

    @Query("""
            select cm.member
            from ClubMembership cm
            where cm.club.id = :clubId
              and cm.member.email <> :excludedEmail
            """)
    List<Member> findMembersByClubIdExceptEmail(
        @Param("clubId") Long clubId,
        @Param("excludedEmail") String excludedEmail
    );
}
