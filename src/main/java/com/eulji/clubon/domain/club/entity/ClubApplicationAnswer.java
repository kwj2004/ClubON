package com.eulji.clubon.domain.club.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
    name = "club_application_answers",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_application_question",
        columnNames = {"application_id", "question_id"}
    )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubApplicationAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private ClubApplication application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private ClubApplicationQuestion question;

    // 질문 수정 이후에도 신청 당시 질문을 보존한다.
    @Column(nullable = false, length = 100)
    private String questionLabel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationQuestionType questionType;

    @ElementCollection
    @CollectionTable(
        name = "club_application_answer_values",
        joinColumns = @JoinColumn(name = "answer_id")
    )
    @Column(name = "answer_value", nullable = false, length = 2000)
    @OrderColumn(name = "value_order")
    private List<String> values = new ArrayList<>();

    public ClubApplicationAnswer(
        ClubApplication application,
        ClubApplicationQuestion question,
        List<String> values
    ) {
        this.application = application;
        this.question = question;
        this.questionLabel = question.getLabel();
        this.questionType = question.getType();
        this.values.addAll(values);
    }
}
