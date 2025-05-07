package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.config.TtsProvider;
import org.springframework.core.io.Resource;

public interface TextToSpeechService {

    Resource convertTextToSpeech(String text, TtsProvider provider);
}
