package com.newworld.saegil.simulator.tts.service;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class TextToSpeechService {

    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public Flux<SpeechResponse> streamSpeech(String text) {
        SpeechPrompt speechPrompt = new SpeechPrompt(text);
        return openAiAudioSpeechModel.stream(speechPrompt);
    }
}
