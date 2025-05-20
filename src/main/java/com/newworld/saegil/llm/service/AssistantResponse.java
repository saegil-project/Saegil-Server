package com.newworld.saegil.llm.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssistantResponse {
    @JsonProperty("question")
    private String userQuestion;

    @JsonProperty("response")
    private String assistantAnswer;

    @JsonProperty("threadId")
    private String threadId;
}
