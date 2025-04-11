package com.newworld.saegil.simulator.tts.controller;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.simulator.tts.dto.TextToSpeechRequest;
import com.newworld.saegil.simulator.tts.service.TextToSpeechService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/tts")
public class TextToSpeechController {

    private final TextToSpeechService textToSpeechService;

    public TextToSpeechController(TextToSpeechService textToSpeechService) {
        this.textToSpeechService = textToSpeechService;
    }

    @Operation(
            summary = "텍스트에서 오디오 스트리밍",
            description = "텍스트를 음성으로 변환하고 오디오를 스트리밍합니다",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @PostMapping(value = "/stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Flux<DataBuffer> streamAudio(@RequestBody TextToSpeechRequest request) {
        return textToSpeechService.streamSpeech(request.text())
                                  .map(response -> {
                                      byte[] audioBytes = response.getResult().getOutput();
                                      return new DefaultDataBufferFactory().wrap(audioBytes);
                                  });
    }
}
