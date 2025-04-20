package com.newworld.saegil.llm.service;

import com.newworld.saegil.llm.controller.AssistantRequest;
import com.newworld.saegil.llm.controller.AssistantResponse;
import com.newworld.saegil.llm.controller.ChatGptAudioUrlRequest;
import com.newworld.saegil.llm.controller.ChatGptSttRequest;
import com.newworld.saegil.llm.controller.ChatGptTextRequest;
import com.newworld.saegil.llm.controller.SpeechToTextUrlRequest;
import com.newworld.saegil.llm.controller.TextToSpeechRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface LlmService {

    Resource textToSpeech(TextToSpeechRequest request);

    String speechToTextFromAudioUrl(SpeechToTextUrlRequest request);

    String speechToTextFromAudioFile(MultipartFile multipartFile);

    String receiveChatGptResponseFromText(ChatGptTextRequest request);

    String receiveChatGptResponseFromSttText(ChatGptSttRequest request);

    String receiveChatGptResponseFromAudioUrl(ChatGptAudioUrlRequest request);

    String receiveChatGptResponseFromAudioFile(MultipartFile multipartFile);

    Resource receiveSttChatGptTtsResponseFromAudioFile(MultipartFile multipartFile);
    
    // Assistant API 관련 메서드
    AssistantResponse getAssistantResponse(AssistantRequest request, String threadId);
    
    AssistantResponse getAssistantResponseFromAudioFile(MultipartFile multipartFile, String threadId);
    
    Resource getAssistantAudioResponse(AssistantRequest request, String threadId, String provider);
    
    Resource getAssistantAudioResponseFromAudioFile(MultipartFile multipartFile, String threadId, String provider);
}
