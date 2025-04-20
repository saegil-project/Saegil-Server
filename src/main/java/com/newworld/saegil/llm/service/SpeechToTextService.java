package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.controller.SpeechToTextUrlRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SpeechToTextService {
    String speechToTextFromAudioUrl(SpeechToTextUrlRequest request);
    String speechToTextFromAudioFile(MultipartFile multipartFile);
}
