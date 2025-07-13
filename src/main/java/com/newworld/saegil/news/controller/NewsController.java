package com.newworld.saegil.news.controller;

import com.newworld.saegil.authentication.annotation.AuthUser;
import com.newworld.saegil.authentication.dto.AuthUserInfo;
import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.global.swagger.ApiResponseCode;
import com.newworld.saegil.news.domain.NewsCategory;
import com.newworld.saegil.news.service.NewsDto;
import com.newworld.saegil.news.service.NewsQuizDto;
import com.newworld.saegil.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스 API")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/categories")
    @Operation(
            summary = "뉴스 카테고리 목록 조회",
            description = "뉴스 카테고리 목록을 조회합니다."
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "뉴스 카테고리 목록 조회 성공")
    public ResponseEntity<List<ReadNewsCategoryResponse>> readCategories() {
        final List<NewsCategory> categories = newsService.readAllCategories();
        final List<ReadNewsCategoryResponse> responses = categories.stream()
                                                                   .map(ReadNewsCategoryResponse::from)
                                                                   .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/interests")
    @Operation(
            summary = "사용자 뉴스 관심사 목록 추가",
            description = "사용자의 뉴스 관심사 목록을 추가합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = ApiResponseCode.CREATED, description = "사용자 뉴스 관심사 목록 추가 성공")
    public ResponseEntity<Void> setUserInterests(
            @AuthUser final AuthUserInfo authUserInfo,
            @RequestBody final CreateUserNewsInterestsRequest request
    ) {
        newsService.setUserInterests(authUserInfo.userId(), request.interests());

        return ResponseEntity.created(URI.create("/api/v1/news/interests"))
                             .build();
    }

    @GetMapping("/interests")
    @Operation(
            summary = "사용자 뉴스 관심사 목록 조회",
            description = "사용자의 뉴스 관심사 목록을 조회합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "사용자 뉴스 관심사 목록 조회 성공")
    public ResponseEntity<List<ReadNewsCategoryResponse>> readUserInterests(
            @AuthUser final AuthUserInfo authUserInfo
    ) {
        final List<NewsCategory> userInterests = newsService.readUserInterests(authUserInfo.userId());
        final List<ReadNewsCategoryResponse> responses = userInterests.stream()
                                                                      .map(ReadNewsCategoryResponse::from)
                                                                      .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @Operation(
            summary = "사용자 관심사에 해당하는 뉴스 목록 조회",
            description = "사용자 관심사에 해당하는 뉴스 목록을 조회합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = ApiResponseCode.OK, description = "사용자 관심 뉴스 목록 조회 성공")
    public ResponseEntity<List<ReadNewsResponse>> readNews(
            @AuthUser final AuthUserInfo authUserInfo,

            @Parameter(description = "조회할 뉴스 개수 (기본값 5)", example = "5")
            @RequestParam(required = false, defaultValue = "5") final int size
    ) {
        final LocalDate date = LocalDate.now();
        final List<NewsDto> result = newsService.readUserInterestNews(authUserInfo.userId(), date, size);
        final List<ReadNewsResponse> responses = result.stream()
                                                       .map(ReadNewsResponse::from)
                                                       .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<NewsQuizResponse> readNewsQuiz(
            @Parameter(description = "뉴스 식별자", example = "1")
            @PathVariable final Long newsId
    ) {
        final NewsQuizDto newsQuizDto = newsService.readQuizByNewsId(newsId);
        final NewsQuizResponse response = NewsQuizResponse.from(newsQuizDto);

        return ResponseEntity.ok(response);
    }
} 
