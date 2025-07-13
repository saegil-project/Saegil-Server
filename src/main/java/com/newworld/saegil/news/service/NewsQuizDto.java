package com.newworld.saegil.news.service;

import com.newworld.saegil.news.domain.NewsQuiz;

public record NewsQuizDto(
        Long id,
        NewsDto newsDto,
        String question,
        boolean isTrue,
        String explanation
) {
    public static NewsQuizDto from(final NewsQuiz quiz) {
        return new NewsQuizDto(
                quiz.getId(),
                NewsDto.from(quiz.getNews()),
                quiz.getQuestion(),
                quiz.isTrue(),
                quiz.getExplanation()
        );
    }
}
