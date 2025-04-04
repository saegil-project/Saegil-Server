package com.newworld.saegil.simulator.conversation.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * Process audio file to get text transcription and GPT answer,
     * then convert the answer back to speech
     *
     * @param audioFile The audio file to process
     * @return Flux of DataBuffer containing the audio response
     */
    public Flux<DataBuffer> processAudioConversation(MultipartFile audioFile) {
        // Step 1: Transcribe audio to text and get GPT answer
        return speechToTextService.transcribeAndAnswer(audioFile)
                                  .doOnNext(result -> lastConversation = result)
                                  .flatMapMany(result -> {
                                      String answer = result.get("answer");
                                      // Step 2: Convert GPT answer to speech and stream it
                                      return textToSpeechService.streamSpeech(answer)
                                                                .map(response -> {
                                                                    byte[] audioBytes = response.getResult()
                                                                                                .getOutput();
                                                                    return new DefaultDataBufferFactory().wrap(
                                                                            audioBytes);
                                                                });
                                  });
    }

    /**
     * Process audio file bytes to get text transcription and GPT answer,
     * then convert the answer back to speech
     *
     * @param audioFileBytes The audio file bytes to process
     * @return Flux of DataBuffer containing the audio response
     */
    public Flux<DataBuffer> processAudioConversation(byte[] audioFileBytes) {
        // Step 1: Transcribe audio to text and get GPT answer
        return speechToTextService.transcribeAndAnswer(audioFileBytes)
                                  .doOnNext(result -> lastConversation = result)
                                  .flatMapMany(result -> {
                                      String answer = result.get("answer");
                                      // Step 2: Convert GPT answer to speech and stream it
                                      return textToSpeechService.streamSpeech(answer)
                                                                .map(response -> {
                                                                    byte[] audioBytes = response.getResult()
                                                                                                .getOutput();
                                                                    return new DefaultDataBufferFactory().wrap(
                                                                            audioBytes);
                                                                });
                                  });
    }

}
