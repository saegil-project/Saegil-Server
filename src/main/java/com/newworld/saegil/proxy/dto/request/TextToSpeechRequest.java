package com.newworld.saegil.proxy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for text-to-speech conversion.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextToSpeechRequest {

    /**
     * The text to convert to speech
     */
    private String text;
}
