package com.newworld.saegil.user.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 API")
public class UserController {

    @GetMapping("/me")
    @Operation(
            summary = "유저 본인 정보 조회",
            description = "access token을 통해 유저 본인 정보를 조회합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_AUTH)
    )
    @ApiResponse(responseCode = "200", description = "유저 본인 정보 조회 성공")
    public ResponseEntity<ReadUserResponse> readUserInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
    ) {
        return null;
    }
}
