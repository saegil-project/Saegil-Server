package com.newworld.saegil.simulation.controller;

import com.newworld.saegil.simulation.service.ScenarioDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReadScenarioResponse(

        @Schema(description = "상황 식별자", example = "1")
        Long id,

        @Schema(description = "상황 이름", example = "시나리오 1")
        String name,

        @Schema(description = "상황 아이콘 이미지 URL", example = "https://example.com/icon.jpg")
        String iconImageUrl
) {

    public static ReadScenarioResponse from(final ScenarioDto scenarioDto) {
        return new ReadScenarioResponse(
                scenarioDto.id(),
                scenarioDto.name(),
                scenarioDto.iconImageUrl()
        );
    }
}
