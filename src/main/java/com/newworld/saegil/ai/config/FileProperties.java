package com.newworld.saegil.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "llm.file")
public record FileProperties(
        String resultFileName,
        String ttsResultFileName
) {
}
