package com.newworld.saegil.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "llm.proxy")
public record ProxyProperties(
        String assistantAudioFromFilePath,
        String assistantUploadPath,
        String textToSpeechPath
) {
}
