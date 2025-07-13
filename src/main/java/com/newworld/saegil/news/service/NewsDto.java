package com.newworld.saegil.news.service;

import com.newworld.saegil.news.domain.News;
import com.newworld.saegil.news.domain.NewsCategory;

import java.time.LocalDate;

public record NewsDto(
        Long id,
        String title,
        NewsCategory category,
        String videoUrl,
        String thumbnailUrl,
        LocalDate date
) {

    public static NewsDto from(final News news) {
        return new NewsDto(
                news.getId(),
                news.getTitle(),
                news.getCategory(),
                news.getVideoUrl(),
                news.getThumbnailUrl(),
                news.getDate()
        );
    }
}
