package com.newworld.saegil.proxy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for ChatGPT responses using text.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptTextRequest {

    /**
     * The text query to send to ChatGPT
     */
    private String text;
}
