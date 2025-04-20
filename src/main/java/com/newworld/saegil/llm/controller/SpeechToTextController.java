package com.newworld.saegil.llm.controller;

import com.newworld.saegil.llm.service.SpeechToTextService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/llm/speech-to-text")
@RequiredArgsConstructor
@Tag(name = "Speech-to-Text API", description = "음성을 텍스트로 변환하는 API")
public class SpeechToTextController {

    private final SpeechToTextService speechToTextService;

    @PostMapping("/audio-url")
    public ResponseEntity<SpeechToTextResponse> speechToTextFromUrl(@RequestBody SpeechToTextUrlRequest request) {
        log.info("Received speech-to-text URL request: {}", request.audioUrl());
        final String text = speechToTextService.speechToTextFromAudioUrl(request);
        final SpeechToTextResponse response = new SpeechToTextResponse(text);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SpeechToTextResponse> speechToTextFromFile(@RequestPart("file") MultipartFile multipartFile) {
        log.info("Received speech-to-text file upload request: {}", multipartFile.getOriginalFilename());
        final String text = speechToTextService.speechToTextFromAudioFile(multipartFile);
        final SpeechToTextResponse response = new SpeechToTextResponse(text);

        return ResponseEntity.ok(response);
    }
}