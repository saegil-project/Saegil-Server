package com.newworld.saegil.simulator.stt.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class SpeechToTextService {

    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final OpenAiChatModel chatModel;

    public Mono<Map<String, String>> transcribeAndGetAnswer(final byte[] audioFileBytes) {
        return Mono.fromCallable(() -> generateConversation(audioFileBytes))
                   .subscribeOn(Schedulers.boundedElastic());
    }

    private Map<String, String> generateConversation(final byte[] audioFileBytes) {
        final Resource audioResource = convertToResource(audioFileBytes);
        final String transcribedText = openAiAudioTranscriptionModel.call(audioResource);
        final String answer = chatModel.call(transcribedText);

        return Map.of(
                "question", transcribedText,
                "answer", answer
        );
    }

    private Resource convertToResource(final byte[] audioFileBytes) {
        final InputStream audioStream = new ByteArrayInputStream(audioFileBytes);

        return new InputStreamResource(audioStream) {
            @Override
            public String getFilename() {
                return "audio.wav";
            }
        };
    }
}
