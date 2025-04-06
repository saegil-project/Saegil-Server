package com.newworld.saegil.user.controller;

import com.newworld.saegil.configuration.SwaggerConfiguration;
import com.newworld.saegil.user.service.UserDto;
import com.newworld.saegil.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(
            summary = "유저 본인 정보 조회",
            description = "access token을 통해 유저 본인 정보를 조회합니다.",
            security = @SecurityRequirement(name = SwaggerConfiguration.SERVICE_SECURITY_SCHEME_NAME)
    )
    @ApiResponse(responseCode = "200", description = "유저 본인 정보 조회 성공")
    public ResponseEntity<ReadUserResponse> readUserInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
    ) {
        // TODO: 유저 정보 조회 기능 개발 후 삭제
        return ResponseEntity.ok(new ReadUserResponse(
                1L,
                "김주민",
                "neighbor_kim@naver.com",
                "https://i.namu.wiki/i/RYsQTAH1KBL6UhqDOp12H5MEk69vd4WroI0bs-hU5ot2HXsvhkf6zjarDYtSXRy4qVJ3b6ogUhsycLcBbyiiqrlajTNKsoPkKj9w1TuRbbqv8glhW9bHLmpxcirJMHue3Qt22jAeAW3bk6eE4AeekQ.svg"
        ));
    }

    // TODO: 인증된 사용자 정보로 조회하는 API로 변경
    @GetMapping("/{id}")
    public ResponseEntity<ReadUserResponse> readById(@PathVariable final Long id) {
        final UserDto userDto = userService.readById(id);
        final ReadUserResponse response = ReadUserResponse.from(userDto);

        return ResponseEntity.ok(response);
    }
}
