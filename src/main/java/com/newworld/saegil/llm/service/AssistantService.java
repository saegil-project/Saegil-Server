package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.controller.AssistantRequest;
import com.newworld.saegil.llm.controller.AssistantResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AssistantService {
    AssistantResponse getAssistantResponse(AssistantRequest request, String threadId);
    AssistantResponse getAssistantResponseFromAudioFile(MultipartFile multipartFile, String threadId);
    Resource getAssistantAudioResponse(AssistantRequest request, String threadId, String provider);
    Resource getAssistantAudioResponseFromAudioFile(MultipartFile multipartFile, String threadId, String provider);
}
