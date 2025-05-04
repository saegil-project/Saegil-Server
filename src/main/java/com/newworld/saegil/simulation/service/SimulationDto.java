package com.newworld.saegil.simulation.service;

import com.newworld.saegil.simulation.domain.Simulation;

import java.time.LocalDateTime;

public record SimulationDto(
        Long id,
        ScenarioDto scenario,
        Long userId,
        LocalDateTime createdAt
) {

    public static SimulationDto from(final Simulation simulation) {
        return new SimulationDto(
                simulation.getId(),
                ScenarioDto.from(simulation.getScenario()),
                simulation.getUser().getId(),
                simulation.getCreatedAt()
        );
    }
}
