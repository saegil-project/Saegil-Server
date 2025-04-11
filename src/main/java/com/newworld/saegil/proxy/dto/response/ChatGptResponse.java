package com.newworld.saegil.proxy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for ChatGPT API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptResponse {

    /**
     * The response text from ChatGPT
     */
    private String response;
}
