package com.newworld.saegil.llm.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.newworld.saegil.llm.controller.ChatGptAudioUrlRequest;
import com.newworld.saegil.llm.controller.ChatGptSttRequest;
import com.newworld.saegil.llm.controller.ChatGptTextRequest;
import com.newworld.saegil.llm.controller.SpeechToTextUrlRequest;
import com.newworld.saegil.llm.controller.TextToSpeechRequest;

public interface LlmService {

    Resource textToSpeech(TextToSpeechRequest request);

    String speechToTextFromAudioUrl(SpeechToTextUrlRequest request);

    String speechToTextFromAudioFile(MultipartFile multipartFile);

    String receiveChatGptResponseFromText(ChatGptTextRequest request);

    String receiveChatGptResponseFromSttText(ChatGptSttRequest request);

    String receiveChatGptResponseFromAudioUrl(ChatGptAudioUrlRequest request);

    String receiveChatGptResponseFromAudioFile(MultipartFile multipartFile);

    Resource receiveSttChatGptTtsResponseFromAudioFile(MultipartFile multipartFile);
}
