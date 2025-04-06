package com.newworld.saegil.authentication.controller;

import com.newworld.saegil.authentication.service.AuthenticationService;
import com.newworld.saegil.configuration.SwaggerConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "인증 API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/{oauth2Type}")
    @Operation(
            summary = "OAuth 2.0 로그인 페이지로 redirect",
            description = """
                    OAuth 2.0 로그인 페이지로 redirect합니다.\n
                    로그인 이후 redirect 되는 url의 `code` query parameter에 Authorization code가 포함되어 있습니다.
                    """
    )
    @ApiResponse(responseCode = "302", description = "OAuth 2.0 로그인 페이지로 redirect 성공")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @Parameter(description = "OAuth 2.0 Type (대소문자 상관 없음)", example = "KAKAO")
            @PathVariable final String oauth2Type
    ) {
        final String redirectUrl = authenticationService.getAuthCodeRequestUrl(oauth2Type);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @PostMapping("/login/{oauth2Type}")
    @Operation(
            summary = "OAuth 2.0 로그인",
            description = "OAuth 2.0 Authorization code를 통해 로그인합니다."
    )
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    public ResponseEntity<LoginInformationResponse> login(
            @PathVariable final String oauth2Type,
            @RequestBody @Valid final LoginRequest request
    ) {
        return null;
    }

    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "로그아웃합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    public ResponseEntity<Void> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken,
            @RequestBody @Valid final LogoutRequest request
    ) {
        return null;
    }

    @GetMapping("/validate-token")
    @Operation(
            summary = "Access Token 유효성 검사",
            description = "Access Token의 유효성을 검사합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = "200", description = "Access Token 유효성 검사 성공")
    public ResponseEntity<ValidateTokenResponse> validateToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken
    ) {
        return null;
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "새 토큰 발급",
            description = "Refresh Token으로 새로운 Access Token과 Refresh Token을 발급합니다."
    )
    @ApiResponse(responseCode = "200", description = "새 토큰 발급 성공")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @RequestBody @Valid final TokenRefreshRequest request
    ) {
        return null;
    }

    @PostMapping("/withdrawal")
    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴를 진행합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공")
    public ResponseEntity<Void> withdrawal(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken,
            @RequestBody @Valid final WithdrawalRequest request
    ) {
        return null;
    }
}
