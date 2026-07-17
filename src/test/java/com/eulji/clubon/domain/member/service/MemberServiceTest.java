package com.eulji.clubon.domain.member.service;

import com.eulji.clubon.domain.auth.repository.EmailVerificationRepository;
import com.eulji.clubon.domain.auth.repository.PasswordResetRepository;
import com.eulji.clubon.domain.auth.repository.RefreshTokenRepository;
import com.eulji.clubon.domain.club.entity.Club;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.entity.ClubMembership;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.department.service.DepartmentService;
import com.eulji.clubon.domain.member.dto.WithdrawMemberRequest;
import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.domain.member.entity.MemberStatus;
import com.eulji.clubon.domain.member.entity.Role;
import com.eulji.clubon.domain.member.repository.MemberRepository;
import com.eulji.clubon.global.error.InvalidPasswordException;
import com.eulji.clubon.global.error.LastClubAdminException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock MemberRepository memberRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock DepartmentService departmentService;
    @Mock RefreshTokenRepository refreshTokenRepository;
    @Mock PasswordResetRepository passwordResetRepository;
    @Mock EmailVerificationRepository emailVerificationRepository;
    @Mock ClubMembershipRepository clubMembershipRepository;

    private MemberService memberService;
    private Member member;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(
                memberRepository,
                passwordEncoder,
                departmentService,
                refreshTokenRepository,
                passwordResetRepository,
                emailVerificationRepository,
                clubMembershipRepository
        );
        member = Member.builder()
                .email("member@eulji.ac.kr")
                .password("encoded-password")
                .name("홍길동")
                .studentId("2026000001")
                .department("간호학과")
                .role(Role.ROLE_STUDENT)
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    @Test
    void withdrawAnonymizesMemberAndClearsAuthenticationData() {
        when(memberRepository.findByEmail("member@eulji.ac.kr")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("CurrentPassword1!", "encoded-password")).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("unusable-password");
        when(clubMembershipRepository.findAllByMemberAndRole(member, ClubMemberRole.ADMIN))
                .thenReturn(List.of());

        memberService.withdraw("member@eulji.ac.kr", new WithdrawMemberRequest("CurrentPassword1!"));

        assertThat(member.getStatus()).isEqualTo(MemberStatus.WITHDRAWN);
        assertThat(member.getWithdrawnAt()).isNotNull();
        assertThat(member.getEmail()).startsWith("withdrawn-").endsWith("@invalid.local");
        assertThat(member.getEmail()).isNotEqualTo("member@eulji.ac.kr");
        assertThat(member.getStudentId()).hasSize(10).startsWith("W");
        assertThat(member.getName()).isEqualTo("탈퇴회원");
        assertThat(member.getDepartment()).isEqualTo("탈퇴");
        verify(refreshTokenRepository).deleteAllByMember(member);
        verify(passwordResetRepository).deleteAllByMember(member);
        verify(emailVerificationRepository).deleteAllByEmail("member@eulji.ac.kr");
        verify(clubMembershipRepository).deleteAllByMember(member);
    }

    @Test
    void withdrawRejectsIncorrectPassword() {
        when(memberRepository.findByEmail("member@eulji.ac.kr")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("wrong", "encoded-password")).thenReturn(false);

        assertThatThrownBy(() -> memberService.withdraw(
                "member@eulji.ac.kr",
                new WithdrawMemberRequest("wrong")
        )).isInstanceOf(InvalidPasswordException.class);

        verify(refreshTokenRepository, never()).deleteAllByMember(member);
    }

    @Test
    void withdrawRejectsLastClubAdmin() {
        Club club = org.mockito.Mockito.mock(Club.class);
        ClubMembership membership = org.mockito.Mockito.mock(ClubMembership.class);
        when(memberRepository.findByEmail("member@eulji.ac.kr")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("CurrentPassword1!", "encoded-password")).thenReturn(true);
        when(clubMembershipRepository.findAllByMemberAndRole(member, ClubMemberRole.ADMIN))
                .thenReturn(List.of(membership));
        when(membership.getClub()).thenReturn(club);
        when(club.getId()).thenReturn(3L);
        when(clubMembershipRepository.countByClub_IdAndRole(3L, ClubMemberRole.ADMIN)).thenReturn(1L);

        assertThatThrownBy(() -> memberService.withdraw(
                "member@eulji.ac.kr",
                new WithdrawMemberRequest("CurrentPassword1!")
        )).isInstanceOf(LastClubAdminException.class);

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        verify(refreshTokenRepository, never()).deleteAllByMember(member);
    }
}
