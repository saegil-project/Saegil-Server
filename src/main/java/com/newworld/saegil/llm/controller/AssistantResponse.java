package com.newworld.saegil.llm.controller;

public record AssistantResponse(
        String response,
        String threadId,
        String text
) {
}