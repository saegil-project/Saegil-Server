package com.newworld.saegil.simulation.controller;

import com.newworld.saegil.authentication.annotation.AuthUser;
import com.newworld.saegil.authentication.dto.AuthUserInfo;
import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.global.swagger.ApiResponseCode;
import com.newworld.saegil.simulation.service.ReadMessagesResult;
import com.newworld.saegil.simulation.service.SimulationDto;
import com.newworld.saegil.simulation.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/simulations")
@RequiredArgsConstructor
@Tag(name = "Simulation", description = "시뮬레이션 기록 API")
public class SimulationController {

    private final SimulationService simulationService;

    @GetMapping
    @Operation(
            summary = "시뮬레이션 기록 목록 조회",
            description = "시뮬레이션 기록 목록을 조회합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "시뮬레이션 기록 목록 조회 성공")
    public ResponseEntity<List<ReadSimulationResponse>> readAllSimulations(@AuthUser final AuthUserInfo authUserInfo) {
        final List<SimulationDto> simulationDtos = simulationService.readAllSimulationHistories(authUserInfo.userId());
        final List<ReadSimulationResponse> response = simulationDtos.stream()
                                                                    .map(ReadSimulationResponse::from)
                                                                    .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{simulationId}/messages")
    @Operation(
            summary = "시뮬레이션 대화 메시지 내용 목록 조회",
            description = "하나의 시뮬레이션 대화 메시지 내용 목록을 조회합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "시뮬레이션 대화 메시지 내용 목록 조회 성공")
    public ResponseEntity<ReadSimulationMessagesResponse> readAllMessagesBySimulationId(
            @Parameter(description = "시뮬레이션 기록 식별자", example = "1")
            @PathVariable final Long simulationId
    ) {
        final ReadMessagesResult result = simulationService.readAllMessagesBySimulationId(simulationId);
        final ReadSimulationMessagesResponse response = ReadSimulationMessagesResponse.from(result);

        return ResponseEntity.ok(response);
    }
}
