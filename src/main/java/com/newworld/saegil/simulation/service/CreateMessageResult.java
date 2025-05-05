package com.newworld.saegil.simulation.service;

public record CreateMessageResult(
        String threadId,
        String userQuestion,
        String assistantAnswer
) {
}
