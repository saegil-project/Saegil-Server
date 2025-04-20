package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.controller.ChatGptAudioUrlRequest;
import com.newworld.saegil.llm.controller.ChatGptSttRequest;
import com.newworld.saegil.llm.controller.ChatGptTextRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ChatGptService {
    String receiveChatGptResponseFromText(ChatGptTextRequest request);
    String receiveChatGptResponseFromSttText(ChatGptSttRequest request);
    String receiveChatGptResponseFromAudioUrl(ChatGptAudioUrlRequest request);
    String receiveChatGptResponseFromAudioFile(MultipartFile multipartFile);
}
