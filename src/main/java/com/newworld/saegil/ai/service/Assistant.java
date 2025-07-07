package com.newworld.saegil.ai.service;

import org.springframework.web.multipart.MultipartFile;

public interface Assistant {

    AssistantResponse getAssistantTextResponseFromAudioFile(
            MultipartFile multipartFile,
            String threadId
    );

    AssistantResponse getAssistantTextResponseFromAudioFile(
            MultipartFile multipartFile,
            String threadId,
            Long scenarioId,
            Long userId
    );

} 