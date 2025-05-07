package com.newworld.saegil.llm.model;

import com.newworld.saegil.llm.config.TtsProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextToSpeechRequest {
    private String text;
    private TtsProvider provider;
}
