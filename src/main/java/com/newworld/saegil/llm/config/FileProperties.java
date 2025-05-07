package com.newworld.saegil.llm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "llm.file")
public record FileProperties(
        String resultFileName,
        String ttsResultFileName // TTS 결과 파일 이름 추가
) {
}
