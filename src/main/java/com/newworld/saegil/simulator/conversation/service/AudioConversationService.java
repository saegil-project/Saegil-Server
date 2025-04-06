package com.newworld.saegil.simulator.conversation.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;

import com.newworld.saegil.simulator.stt.service.SpeechToTextService;
import com.newworld.saegil.simulator.tts.service.TextToSpeechService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AudioConversationService {

    private final SpeechToTextService speechToTextService;
    private final TextToSpeechService textToSpeechService;

    // Store the last conversation for retrieval
    @Getter
    private Map<String, String> lastConversation = new HashMap<>();

    public Flux<DataBuffer> processAudioConversation(final byte[] audioFileBytes) {
        return speechToTextService.transcribeAndGetAnswer(audioFileBytes)
                                  .doOnNext(result -> lastConversation = result)
                                  .flatMapMany(this::streamAnswerAsAudio);
    }

    private Flux<DataBuffer> streamAnswerAsAudio(final Map<String, String> result) {
        final String answer = result.get("answer");

        return textToSpeechService.streamSpeech(answer)
                                  .map(response -> {
                                      byte[] audioBytes = response.getResult().getOutput();
                                      return new DefaultDataBufferFactory().wrap(audioBytes);
                                  });
    }
}
