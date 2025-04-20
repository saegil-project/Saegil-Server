package com.newworld.saegil.llm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "llm.proxy")
public class ProxyProperties {

    private String ttsPath;
    private String sttPath;
    private String sttFromAudioFilePath;
    private String chatgptFromTextPath;
    private String chatgptFromSttTextPath;
    private String chatgptFromAudioUrlPath;
    private String chatgptFromAudioFilePath;
    private String sttChatgptTtsFilePath;
    

    private String assistantPath;
    private String assistantFromAudioFilePath;
    private String assistantAudioPath;
    private String assistantAudioFromFilePath;

}
