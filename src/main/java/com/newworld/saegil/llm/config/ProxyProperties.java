package com.newworld.saegil.llm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "llm.proxy")
public record ProxyProperties(
    String ttsPath,
    String sttPath,
    String sttFromAudioFilePath,
    String chatgptFromTextPath,
    String chatgptFromSttTextPath,
    String chatgptFromAudioUrlPath,
    String chatgptFromAudioFilePath,
    String sttChatgptTtsFilePath,

    String assistantPath,
    String assistantFromAudioFilePath,
    String assistantAudioPath,
    String assistantAudioFromFilePath
) {
}
