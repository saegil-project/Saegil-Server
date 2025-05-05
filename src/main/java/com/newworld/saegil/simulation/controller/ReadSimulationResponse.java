package com.newworld.saegil.simulation.controller;

import com.newworld.saegil.simulation.service.SimulationDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ReadSimulationResponse(
        @Schema(description = "시뮬레이션 기록 식별자", example = "1")
        Long id,

        @Schema(description = "상황 이름", example = "마트에 갔을 때")
        String scenarioName,

        @Schema(description = "상황 아이콘 이미지 URL", example = "https://example.com/icon.jpg")
        String scenarioIconImageUrl,

        @Schema(description = "시뮬레이션 기록 생성 시간", example = "2025-05-05T12:00:00")
        LocalDateTime createdAt
) {

    public static ReadSimulationResponse from(final SimulationDto simulationDto) {
        return new ReadSimulationResponse(
                simulationDto.id(),
                simulationDto.scenario().name(),
                simulationDto.scenario().iconImageUrl(),
                simulationDto.createdAt()
        );
    }
}
