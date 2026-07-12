package com.eulji.clubon.domain.inquiry.entity;

import com.eulji.clubon.domain.member.entity.Member;
import com.eulji.clubon.global.error.AlreadyAnsweredInquiryException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "inquiries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InquiryType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String attachmentUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InquiryStatus status;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime answeredAt;

    public Inquiry(
        Member member,
        InquiryType type,
        String title,
        String content,
        String attachmentUrl
    ) {
        this.member = member;
        this.type = type;
        this.title = title;
        this.content = content;
        this.attachmentUrl = attachmentUrl;
        this.status = InquiryStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void answer(String answer) {
        if (this.status == InquiryStatus.ANSWERED) {
            throw new AlreadyAnsweredInquiryException();
        }

        this.answer = answer;
        this.status = InquiryStatus.ANSWERED;
        this.answeredAt = LocalDateTime.now();
    }
}
//문의 엔티티
