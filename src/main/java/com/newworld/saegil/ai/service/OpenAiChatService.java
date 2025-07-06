package com.newworld.saegil.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAiChatService implements OpenAiChat {

    private final ChatModel chatModel;

    @Override
    public byte[] getOpenAiResponse(final MultipartFile multipartFile) throws Exception {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("음성 파일이 제공되지 않았습니다.");
        }

        try {
            Resource audioResource = new MultipartInputStreamFileResource(multipartFile);
            
            var userMessage = UserMessage.builder()
                                         .text("Please respond to this audio message")
                                         .media(List.of(new Media(MimeTypeUtils.parseMimeType("audio/mp3"), audioResource)))
                                         .build();
            
            ChatResponse chatResponse = this.chatModel.call(new Prompt(List.of(userMessage),
                                                                       OpenAiChatOptions.builder()
                                                                                        .model("gpt-4o-audio-preview")
                                                                                        .outputModalities(List.of("text", "audio"))
                                                                                        .outputAudio(new OpenAiApi.ChatCompletionRequest.AudioParameters(
                                                                                                OpenAiApi.ChatCompletionRequest.AudioParameters.Voice.ALLOY,
                                                                                                OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat.MP3))
                                                                                        .build()));

            if (chatResponse.getResult().getOutput().getMedia().isEmpty()) {
                throw new RuntimeException("OpenAI로부터 오디오 응답을 받지 못했습니다.");
            }

            log.info(chatResponse.getResult().getOutput().getText());

            return chatResponse.getResult().getOutput().getMedia().getFirst().getDataAsByteArray();
            
        } catch (IOException e) {
            throw new Exception("음성 파일 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("OpenAI API 호출 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}