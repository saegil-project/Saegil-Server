package com.newworld.saegil.proxy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for speech-to-text conversion using an audio URL.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechToTextUrlRequest {

    /**
     * The URL of the audio file to convert to text
     */
    private String audioUrl;
}
