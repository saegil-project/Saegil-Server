package com.newworld.saegil.ai.service;

import org.springframework.web.multipart.MultipartFile;

public interface OpenAiChat {
    byte[] getOpenAiResponse(MultipartFile multipartFile) throws Exception;
}
