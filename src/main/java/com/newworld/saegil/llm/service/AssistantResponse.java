package com.newworld.saegil.llm.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AssistantResponse(
        @JsonProperty("question") String userQuestion,
        @JsonProperty("response") String assistantAnswer,
        @JsonProperty("threadId") String threadId
) {
}
