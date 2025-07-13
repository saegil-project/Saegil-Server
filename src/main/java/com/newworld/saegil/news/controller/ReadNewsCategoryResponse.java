package com.newworld.saegil.news.controller;

import com.newworld.saegil.news.domain.NewsCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReadNewsCategoryResponse(

        @Schema(description = "카테고리 enum (관심사 선택 post 요청 시 사용)", example = "POLITICS")
        String type,

        @Schema(description = "카테고리 이름 (사용자에게 표시할 한국어)", example = "정치")
        String name
) {

    public static ReadNewsCategoryResponse from(final NewsCategory category) {
        return new ReadNewsCategoryResponse(category.name(), category.getName());
    }
}
