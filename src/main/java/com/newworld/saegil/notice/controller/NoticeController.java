package com.newworld.saegil.notice.controller;

import com.newworld.saegil.global.swagger.ApiResponseCode;
import com.newworld.saegil.notice.service.NoticeService;
import com.newworld.saegil.notice.service.ReadNoticesResult;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 API")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/sources")
    @Operation(
            summary = "공지사항 기관 목록 조회",
            description = "공지사항 기관 목록을 조회합니다."
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "공지사항 기관 목록 조회 성공")
    public ResponseEntity<List<ReadNoticeSourceItemResponse>> readAllSources() {
        List<ReadNoticeSourceItemResponse> sources = new ArrayList<>();
//        sources.add(new ReadNoticeSourceItemResponse(1L, "남북하나재단"));
//        sources.add(new ReadNoticeSourceItemResponse(2L, "통일부"));
        sources.add(new ReadNoticeSourceItemResponse(3L, "서울외국인포털"));

        return ResponseEntity.ok(sources);
    }

    @GetMapping
    @Operation(
            summary = "공지사항 목록 조회",
            description = "공지사항 목록 전체 조회, 검색, 필터링이 가능합니다. 무한스크롤 방식으로 lastId를 기준으로 데이터를 가져옵니다."
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "공지사항 목록 조회 성공")
    public ResponseEntity<ReadNoticesResponse> readAll(
            @Parameter(description = "검색어 (필수 x)", example = "채용")
            @RequestParam(required = false) final String query,

            @Parameter(description = "기관명 필터 (ex. 남북하나재단, 통일부) (필수 x)", example = "남북하나재단")
            @RequestParam(required = false, defaultValue = "3") final Long sourceId,

            @Parameter(description = "마지막으로 조회된 공지사항 ID (무한스크롤 방식) (첫 요청 = null)", example = "101")
            @RequestParam(required = false) final Long lastId,

            @Parameter(description = "페이지당 항목 개수 (필수 x)", example = "10")
            @RequestParam(defaultValue = "10") final int size
    ) {
        final ReadNoticesResult result = noticeService.readAll(query, sourceId, lastId, size);
        final ReadNoticesResponse response = ReadNoticesResponse.from(result);

        return ResponseEntity.ok(response);
    }
}
