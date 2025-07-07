package com.newworld.saegil.ai.controller;

import com.newworld.saegil.ai.config.TtsProvider;

public record TextToSpeechRequest(String text, TtsProvider provider) {
}
