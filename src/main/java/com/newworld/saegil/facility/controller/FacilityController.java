package com.newworld.saegil.facility.controller;

import com.newworld.saegil.facility.service.FacilityService;
import com.newworld.saegil.global.swagger.ApiResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facilities")
@RequiredArgsConstructor
@Tag(name = "Facility", description = "시설 API")
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping("/nearby")
    @Operation(
            summary = "근처 시설 조회",
            description = "사용자의 현재 위치와 반경을 기준으로 주변 시설을 조회합니다."
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "근처 시설 조회 성공")
    public ResponseEntity<List<ReadFacilityResponse>> readNearbyFacilities(
            @Parameter(description = "현재 위도", example = "37.5326")
            @RequestParam double latitude,

            @Parameter(description = "현재 경도", example = "126.8469")
            @RequestParam double longitude,

            @Parameter(description = "검색 반경 (미터 단위, 예: 500 / 1000 / 5000)", example = "1000")
            @RequestParam final int radius
    ) {
        return null;
    }
}
