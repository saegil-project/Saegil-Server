package com.newworld.saegil.llm.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface SttChatGptTtsService {
    Resource receiveSttChatGptTtsResponseFromAudioFile(MultipartFile multipartFile);
}
