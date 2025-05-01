package com.newworld.saegil.organization.controller;

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
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@Tag(name = "Organization", description = "기관 API")
public class OrganizationController {

    @GetMapping("/nearby")
    @Operation(
            summary = "근처 기관 조회",
            description = "사용자의 현재 위치와 반경을 기준으로 주변 기관을 조회합니다."
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "근처 기관 조회 성공")
    public ResponseEntity<List<ReadOrganizationResponse>> getNearbyOrganizations(
            @Parameter(description = "현재 위도", example = "37.5326")
            @RequestParam double latitude,

            @Parameter(description = "현재 경도", example = "126.8469")
            @RequestParam double longitude,

            @Parameter(description = "검색 반경 (미터 단위, 예: 500 / 1000 / 5000)", example = "1000")
            @RequestParam final int radius
    ) {
        // TODO: 근처 기관 목록 조회 기능 개발 후 삭제
        List<ReadOrganizationResponse> dummyOrganizations = List.of(
                new ReadOrganizationResponse(
                        1L,
                        "한빛종합사회복지관",
                        37.5190344,
                        126.8402373,
                        "09:00 ~ 17:00",
                        "02-2690-8762~4",
                        "서울특별시 양천구 신월로11길 16 (신월동, 한빛종합사회복지관)",
                        0.0
                ),
                new ReadOrganizationResponse(
                        2L,
                        "신정4동 주민센터",
                        37.5245454,
                        126.8558041,
                        "09:00 ~ 17:00",
                        "02-2690-8762~4",
                        "서울특별시 양천구 오목로34길 5",
                        1.5034108898263117
                )
        );

        return ResponseEntity.ok(dummyOrganizations);
    }
}
