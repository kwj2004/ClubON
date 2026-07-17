package com.eulji.clubon.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String studentId;

    @Column(nullable = false, length = 50)
    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default 'ACTIVE'")
    private MemberStatus status;

    private LocalDateTime withdrawnAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Member(String email, String password, String name, String studentId, String department, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.studentId = studentId;
        this.department = department;
        this.role = role;
        this.status = MemberStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void updateProfile(String password, String name, String department) {
        if (password != null) {
            this.password = password;
        }

        if (name != null) {
            this.name = name;
        }

        if (department != null) {
            this.department = department;
        }

        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeRole(Role role) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }

    public void withdraw(String anonymizedEmail, String anonymizedStudentId, String encodedPassword) {
        this.email = anonymizedEmail;
        this.studentId = anonymizedStudentId;
        this.password = encodedPassword;
        this.name = "탈퇴회원";
        this.department = "탈퇴";
        this.status = MemberStatus.WITHDRAWN;
        this.withdrawnAt = LocalDateTime.now();
        this.updatedAt = this.withdrawnAt;
    }
}
