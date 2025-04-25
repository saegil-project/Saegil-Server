package com.newworld.saegil.llm.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProxyAssistantResponse(
        String response,
        String threadId,
        String text
) {
}
