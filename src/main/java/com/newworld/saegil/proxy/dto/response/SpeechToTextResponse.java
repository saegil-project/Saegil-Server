package com.newworld.saegil.proxy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for speech-to-text conversion.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechToTextResponse {

    /**
     * The text converted from speech
     */
    private String text;
}
