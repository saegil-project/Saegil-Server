package com.newworld.saegil.news.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "news_quiz")
public class NewsQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id")
    private News news;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private boolean isTrue;

    @Column(nullable = false)
    private String explanation;

    public NewsQuiz(
            final News news,
            final String question,
            final boolean isTrue,
            final String explanation
    ) {
        this.news = news;
        this.question = question;
        this.isTrue = isTrue;
        this.explanation = explanation;
    }
}
