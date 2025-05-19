package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.model.AssistantResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AssistantService {
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
