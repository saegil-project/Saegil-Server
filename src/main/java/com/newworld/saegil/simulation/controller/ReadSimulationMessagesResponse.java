package com.newworld.saegil.simulation.controller;

import com.newworld.saegil.simulation.service.MessageDto;
import com.newworld.saegil.simulation.service.ReadMessagesResult;

import java.time.LocalDateTime;
import java.util.List;

public record ReadSimulationMessagesResponse(
        String scenarioName,
        List<ReadMessageItemResponse> messages
) {

    public static ReadSimulationMessagesResponse from(final ReadMessagesResult readMessagesResult) {
        final String scenarioName = readMessagesResult.simulation().scenario().name();
        final List<ReadMessageItemResponse> messages = readMessagesResult.messages().stream()
                                                                         .map(ReadMessageItemResponse::from)
                                                                         .toList();

        return new ReadSimulationMessagesResponse(scenarioName, messages);
    }

    record ReadMessageItemResponse(
            Long id,
            boolean isFromUser,
            String contents,
            LocalDateTime createdAt
    ) {
        public static ReadMessageItemResponse from(final MessageDto messageDto) {
            return new ReadMessageItemResponse(
                    messageDto.id(),
                    messageDto.isFromUser(),
                    messageDto.contents(),
                    messageDto.createdAt()
            );
        }
    }
}
