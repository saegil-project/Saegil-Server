package com.newworld.saegil.proxy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for ChatGPT responses using STT-converted text.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptSttRequest {

    /**
     * The STT-converted text to send to ChatGPT
     */
    private String audioText;
}
