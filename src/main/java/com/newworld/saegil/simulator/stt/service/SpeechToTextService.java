package com.newworld.saegil.simulator.stt.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Service
public class SpeechToTextService {

    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final OpenAiChatModel chatModel;

    public Mono<Map<String, String>> transcribeAndAnswer(MultipartFile audioFile) {
        return Mono.fromCallable(() -> {
                       try {
                           InputStream audioStream = audioFile.getInputStream();
                           Resource audioResource = new InputStreamResource(audioStream) {
                               @Override
                               public String getFilename() {
                                   return audioFile.getOriginalFilename();
                               }
                           };
                           return audioResource;
                       } catch (IOException e) {
                           throw new RuntimeException("Failed to process audio file", e);
                       }
                   })
                   .flatMap(this::processAudioResource)
                   .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Map<String, String>> transcribeAndAnswer(byte[] audioFileBytes) {
        return Mono.fromCallable(() -> {
                       InputStream audioStream = new ByteArrayInputStream(audioFileBytes);
                       Resource audioResource = new InputStreamResource(audioStream) {
                           @Override
                           public String getFilename() {
                               return "audio.wav"; // Default filename
                           }
                       };
                       return audioResource;
                   })
                   .flatMap(this::processAudioResource)
                   .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Map<String, String>> processAudioResource(Resource audioResource) {
        return Mono.fromCallable(() -> openAiAudioTranscriptionModel.call(audioResource))
                   .flatMap(transcribedText ->
                           Mono.fromCallable(() -> chatModel.call(transcribedText))
                               .map(answer -> Map.of(
                                       "question", transcribedText,
                                       "answer", answer
                               ))
                   )
                   .subscribeOn(Schedulers.boundedElastic());
    }
}
