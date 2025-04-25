package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.controller.ChatGptAudioUrlRequest;
import com.newworld.saegil.llm.controller.ChatGptSttRequest;
import com.newworld.saegil.llm.controller.ChatGptTextRequest;
import org.springframework.web.multipart.MultipartFile;

public interface ChatGptService {
    String receiveChatGptResponseFromText(final ChatGptTextRequest request);
    String receiveChatGptResponseFromSttText(final ChatGptSttRequest request);
    String receiveChatGptResponseFromAudioUrl(final ChatGptAudioUrlRequest request);
    String receiveChatGptResponseFromAudioFile(final MultipartFile multipartFile);
}
