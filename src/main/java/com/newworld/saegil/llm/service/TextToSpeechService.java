package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.controller.TextToSpeechRequest;
import org.springframework.core.io.Resource;

public interface TextToSpeechService {
    Resource textToSpeech(TextToSpeechRequest request);
}
