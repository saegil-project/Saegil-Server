package com.newworld.saegil.llm.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AssistantService {
    Resource getAssistantAudioResponseFromAudioFile(final MultipartFile multipartFile, final String threadId, final String provider);
}
