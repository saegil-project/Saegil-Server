package com.newworld.saegil.news.controller;

import com.newworld.saegil.news.service.NewsDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReadNewsResponse(

        @Schema(description = "뉴스 식별자", example = "1")
        Long id,

        @Schema(description = "뉴스 제목", example = "장마 실종에 때 아닌 산불 비상 / KBS 2025.07.13.")
        String title,

        @Schema(description = "뉴스 카테고리", example = "재난∙기후∙환경")
        String category,

        @Schema(description = "뉴스 썸네일 이미지 URL", example = "https://img.youtube.com/vi/FPWWs8v1VuQ/0.jpg")
        String thumbnailUrl,

        @Schema(description = "뉴스 날짜", example = "2025-07-13")
        LocalDate date
) {

    public static ReadNewsResponse from(final NewsDto newsDto) {
        return new ReadNewsResponse(
                newsDto.id(),
                newsDto.title(),
                newsDto.category().getName(),
                newsDto.thumbnailUrl(),
                newsDto.date()
        );
    }
}
