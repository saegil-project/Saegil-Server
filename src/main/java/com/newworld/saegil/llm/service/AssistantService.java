package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.controller.AssistantRequest;
import com.newworld.saegil.llm.controller.AssistantResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AssistantService {
    AssistantResponse getAssistantResponse(final AssistantRequest request, final String threadId);
    AssistantResponse getAssistantResponseFromAudioFile(final MultipartFile multipartFile, final String threadId);
    Resource getAssistantAudioResponse(final AssistantRequest request, final String threadId, final String provider);
    Resource getAssistantAudioResponseFromAudioFile(final MultipartFile multipartFile, final String threadId, final String provider);
}
