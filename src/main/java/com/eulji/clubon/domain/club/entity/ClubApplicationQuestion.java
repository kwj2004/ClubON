package com.eulji.clubon.domain.club.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "club_application_questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubApplicationQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creation_request_id")
    private ClubCreationRequest creationRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(nullable = false, length = 100)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationQuestionType type;

    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private int sortOrder;

    @ElementCollection
    @CollectionTable(
        name = "club_application_question_options",
        joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "option_value", nullable = false, length = 100)
    @OrderColumn(name = "option_order")
    private List<String> options = new ArrayList<>();

    public ClubApplicationQuestion(
        ClubCreationRequest creationRequest,
        String label,
        ApplicationQuestionType type,
        boolean required,
        int sortOrder,
        List<String> options
    ) {
        this.creationRequest = creationRequest;
        this.label = label;
        this.type = type;
        this.required = required;
        this.sortOrder = sortOrder;

        if (options != null) {
            this.options.addAll(options);
        }
    }

    public void connectClub(Club club) {
        this.club = club;
    }
}
// 가입 신청서 질문 엔티티 생성
