package com.newworld.saegil.news.controller;

import com.newworld.saegil.news.domain.NewsCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateUserNewsInterestsRequest(

        @NotEmpty(message = "사용자 관심사를 1개 이상 선택해주세요.")
        @Schema(description = "사용자 관심사 리스트 (1개 이상)", example = "[\"SPORTS\", \"ENTERTAINMENT\"]")
        List<NewsCategory> interests
) {
}
