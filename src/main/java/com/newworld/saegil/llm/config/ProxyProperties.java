package com.newworld.saegil.llm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "proxy.llm")
public record ProxyProperties(
        String llmServerUrl,
        String ttsPath,
        String sttPath,
        String sttFromAudioFilePath,
        String chatgptFromTextPath,
        String chatgptFromSttTextPath,
        String chatgptFromAudioUrlPath,
        String chatgptFromAudioFilePath
) {

    public String getTtsPath() {
        return llmServerUrl + ttsPath;
    }

    public String getSttPath() {
        return llmServerUrl + sttPath;
    }

    public String getSttFromAudioFilePath() {
        return llmServerUrl + sttFromAudioFilePath;
    }

    public String getChatgptFromTextPath() {
        return llmServerUrl + chatgptFromTextPath;
    }

    public String getChatgptFromSttTextPath() {
        return llmServerUrl + chatgptFromSttTextPath;
    }

    public String getChatgptFromAudioUrlPath() {
        return llmServerUrl + chatgptFromAudioUrlPath;
    }

    public String getChatgptFromAudioFilePath() {
        return llmServerUrl + chatgptFromAudioFilePath;
    }
}
