package com.newworld.saegil.llm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "llm.proxy")
public record ProxyProperties(
    String assistantAudioFromFilePath,
    String assistantUploadPath,
    String textToSpeechPath // LLM 서버의 Text-to-Speech 엔드포인트 경로
) {
}
