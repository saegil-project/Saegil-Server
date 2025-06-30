package com.newworld.saegil.ai.service;

import com.newworld.saegil.ai.config.TtsProvider;
import org.springframework.core.io.Resource;

public interface TextToSpeech {

    Resource convertTextToSpeech(final String text, final TtsProvider provider);

    byte[] convertTextToSpeechV2(final String text);

} 