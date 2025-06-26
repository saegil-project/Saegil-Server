package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.config.TtsProvider;
import org.springframework.core.io.Resource;

public interface TextToSpeech {

    Resource convertTextToSpeech(final String text, final TtsProvider provider);

} 