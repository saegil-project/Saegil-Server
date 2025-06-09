package com.newworld.saegil.recruitment.controller;

import com.newworld.saegil.global.swagger.ApiResponseCode;
import com.newworld.saegil.location.GeoPoint;
import com.newworld.saegil.recruitment.service.NearbyRecruitmentDto;
import com.newworld.saegil.recruitment.service.RecruitmentService;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitments")
@RequiredArgsConstructor
@Tag(name = "Recruiment", description = "채용 정보 API")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @GetMapping("/nearby")
    @Operation(
            summary = "근처 채용 정보 조회",
            description = "사용자의 현재 위치와 반경을 기준으로 주변 채용 정보를 조회합니다. 지원 마감일이 지난 정보는 제외됩니다."
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "근처 채용 정보 조회 성공")
    public ResponseEntity<List<ReadRecruitmentResponse>> readNearbyRecruitments(
            @Parameter(description = "현재 위도", example = "37.5326")
            @RequestParam double latitude,

            @Parameter(description = "현재 경도", example = "126.8469")
            @RequestParam double longitude,

            @Parameter(description = "검색 반경 (미터 단위, 예: 500 / 1000 / 5000)", example = "1000")
            @RequestParam final int radius
    ) {
        final LocalDateTime requestDateTime = LocalDateTime.now();
        final GeoPoint userGeoPoint = new GeoPoint(latitude, longitude);
        final List<NearbyRecruitmentDto> result = recruitmentService.readNearbyRecruitments(
                userGeoPoint,
                radius,
                requestDateTime
        );
        final List<ReadRecruitmentResponse> response = result.stream()
                                                             .map(ReadRecruitmentResponse::from)
                                                             .toList();

        return ResponseEntity.ok(response);
    }
}
