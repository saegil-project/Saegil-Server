package com.newworld.saegil.simulator.conversation.controller;

import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newworld.saegil.simulator.conversation.service.AudioConversationService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/stt")
@RequiredArgsConstructor
public class AudioConversationController {

    private final AudioConversationService audioConversationService;

    @PostMapping(value = "/audio-to-audio", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Flux<DataBuffer> audioConversation(@RequestBody byte[] audioFileBytes) {
        return audioConversationService.processAudioConversation(audioFileBytes);
    }

    @PostMapping("/last-conversation")
    public ResponseEntity<Map<String, String>> getLastConversation() {
        return ResponseEntity.ok(audioConversationService.getLastConversation());
    }
}
