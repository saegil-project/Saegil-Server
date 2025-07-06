package com.newworld.saegil.ai.controller;

import com.newworld.saegil.ai.service.RealTimeApi;
import com.newworld.saegil.ai.service.RealTimeApiTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RealTimeApiTokenController {

    private final RealTimeApi realTimeApi;

    @GetMapping("/realtime/token")
    public ResponseEntity<RealTimeApiTokenResponse> getRealtimeToken() {
        log.info("Requesting realtime token from OpenAI");
        RealTimeApiTokenResponse response = realTimeApi.createEphemeralToken();
        return ResponseEntity.ok(response);
    }
}
