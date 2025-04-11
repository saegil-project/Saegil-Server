package com.newworld.saegil.proxy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for ChatGPT responses using an audio URL.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptAudioUrlRequest {

    /**
     * The URL of the audio file to convert to text and send to ChatGPT
     */
    private String audioUrl;
}
