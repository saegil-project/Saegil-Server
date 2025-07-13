package com.newworld.saegil.news.controller;

import com.newworld.saegil.news.service.NewsDto;
import com.newworld.saegil.news.service.NewsQuizDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record NewsQuizResponse(

        @Schema(description = "뉴스 퀴즈 식별자", example = "1")
        Long id,

        @Schema(description = "뉴스 제목", example = "장마 실종에 때 아닌 산불 비상 / KBS 2025.07.13.")
        String title,

        @Schema(description = "뉴스 카테고리", example = "재난∙기후∙환경")
        String category,

        @Schema(description = "뉴스 비디오 URL", example = "https://www.youtube.com/watch?v=FPWWs8v1VuQ")
        String videoUrl,

        @Schema(description = "뉴스 썸네일 이미지 URL", example = "https://img.youtube.com/vi/FPWWs8v1VuQ/0.jpg")
        String thumbnailUrl,

        @Schema(description = "뉴스 날짜", example = "2025-07-13")
        LocalDate date,

        @Schema(description = "퀴즈 질문", example = "기온이 오르면 산불 발생 위험이 낮아진다.")
        String question,

        @Schema(description = "퀴즈 정답 (true 면 O, false 면 X", example = "X")
        String answer,

        @Schema(description = "퀴즈 설명", example = "국립산림과학원 연구에 따르면  기온이 1도 오르면 산불 발생 위험이 8% 증가하고, 2도 상승시 13% 증가합니다.")
        String explanation
) {

    public static NewsQuizResponse from(final NewsQuizDto newsQuizDto) {
        final NewsDto newsDto = newsQuizDto.newsDto();
        return new NewsQuizResponse(
                newsQuizDto.id(),
                newsDto.title(),
                newsDto.category().getName(),
                newsDto.videoUrl(),
                newsDto.thumbnailUrl(),
                newsDto.date(),
                newsQuizDto.question(),
                newsQuizDto.isTrue() ? "O" : "X",
                newsQuizDto.explanation()
        );
    }
}
