package com.newworld.saegil.news.domain;

import lombok.Getter;

@Getter
public enum NewsCategory {

    POLITICS("정치"),
    ECONOMY("경제"),
    SOCIETY("사회"),
    CULTURE("문화"),
    IT_SCIENCE("IT∙과학"),
    INTERNATIONAL("국제"),
    DISASTER_CLIMATE_ENVIRONMENT("재난∙기후∙환경"),
    LIFE_HEALTH("생활∙건강"),
    SPORTS("스포츠"),
    ENTERTAINMENT("연예"),
    WEATHER("날씨"),
    ;

    private final String name;

    NewsCategory(final String name) {
        this.name = name;
    }
}
