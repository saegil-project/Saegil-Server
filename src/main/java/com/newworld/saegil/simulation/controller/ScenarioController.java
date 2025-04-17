package com.newworld.saegil.simulation.controller;

import com.newworld.saegil.global.swagger.ApiResponseCode;
import com.newworld.saegil.simulation.service.ScenarioDto;
import com.newworld.saegil.simulation.service.ScenarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scenarios")
@RequiredArgsConstructor
@Tag(name = "Notice", description = "시뮬레이션 시나리오 목록 API")
public class ScenarioController {

    private final ScenarioService scenarioService;

    @GetMapping
    @Operation(
            summary = "시뮬레이션 상황 목록 조회",
            description = "시뮬레이션 상황 목록을 조회합니다."
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "시뮬레이션 상황 목록 조회 성공")
    public ResponseEntity<List<ReadScenarioResponse>> readAll() {
        final List<ScenarioDto> scenarioDtos = scenarioService.readAll();
        final List<ReadScenarioResponse> responses = scenarioDtos.stream()
                .map(ReadScenarioResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
