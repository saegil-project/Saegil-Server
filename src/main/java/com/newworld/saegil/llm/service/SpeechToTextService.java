package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.controller.SpeechToTextUrlRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SpeechToTextService {
    String speechToTextFromAudioUrl(final SpeechToTextUrlRequest request);
    String speechToTextFromAudioFile(final MultipartFile multipartFile);
}
