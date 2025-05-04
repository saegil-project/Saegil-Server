package com.newworld.saegil.simulation.service;

import com.newworld.saegil.simulation.domain.Message;
import com.newworld.saegil.simulation.domain.Simulation;

import java.util.List;

public record ReadMessagesResult(
        SimulationDto simulation,
        List<MessageDto> messages
) {

    public static ReadMessagesResult from(
            final Simulation simulation,
            final List<Message> messages
    ) {
        final SimulationDto simulationDto = SimulationDto.from(simulation);
        final List<MessageDto> messageDtos = messages.stream()
                                                     .map(MessageDto::from)
                                                     .toList();

        return new ReadMessagesResult(
                simulationDto,
                messageDtos
        );
    }
}
