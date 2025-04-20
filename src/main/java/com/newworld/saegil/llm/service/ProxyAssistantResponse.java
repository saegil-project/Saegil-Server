package com.newworld.saegil.llm.service;

public record ProxyAssistantResponse(
        String response,
        String threadId,
        String text
) {
}