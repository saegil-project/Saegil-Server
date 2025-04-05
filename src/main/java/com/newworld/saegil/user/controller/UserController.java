package com.newworld.saegil.user.controller;

import com.newworld.saegil.user.service.UserDto;
import com.newworld.saegil.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO: 인증된 사용자 정보로 조회하는 API로 변경
    @GetMapping("/{id}")
    public ResponseEntity<ReadUserResponse> readById(@PathVariable final Long id) {
        final UserDto userDto = userService.readById(id);
        final ReadUserResponse response = ReadUserResponse.from(userDto);

        return ResponseEntity.ok(response);
    }
}
