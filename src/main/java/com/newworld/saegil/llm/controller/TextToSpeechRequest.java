package com.newworld.saegil.llm.controller;

import com.newworld.saegil.llm.config.TtsProvider;

public record TextToSpeechRequest(String text, TtsProvider provider) {
}
