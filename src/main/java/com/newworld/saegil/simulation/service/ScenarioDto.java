package com.newworld.saegil.simulation.service;

import com.newworld.saegil.simulation.domain.Scenario;

public record ScenarioDto(
        Long id,
        String name,
        String iconImageUrl
) {


    public static ScenarioDto from(final Scenario scenario) {
        return new ScenarioDto(
                scenario.getId(),
                scenario.getName(),
                scenario.getIconImageUrl()
        );
    }
}
