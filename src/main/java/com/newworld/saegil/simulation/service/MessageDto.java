package com.newworld.saegil.simulation.service;

import com.newworld.saegil.simulation.domain.Message;

import java.time.LocalDateTime;

public record MessageDto(
        Long id,
        Long simulationId,
        boolean isFromUser,
        String contents,
        LocalDateTime createdAt
) {
    public static MessageDto from(Message message) {
        return new MessageDto(
                message.getId(),
                message.getSimulation().getId(),
                message.isFromUser(),
                message.getContents(),
                message.getCreatedAt()
        );
    }
}
