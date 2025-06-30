package com.newworld.saegil.file.controller;

import com.newworld.saegil.file.service.FileDto;
import com.newworld.saegil.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "File", description = "파일 API")
public class FileController {

    private final FileService fileService;

    @GetMapping("/{storeName}")
    @Operation(summary = "서버에 저장된 파일 요청")
    public ResponseEntity<Resource> read(
            @Parameter(description = "파일 이름", example = "asdfasdfasdf.png")
            @PathVariable final String storeName
    ) throws IOException {
        final FileDto fileDto = fileService.read(storeName);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(fileDto.contentType());

        return new ResponseEntity<>(fileDto.file(), headers, HttpStatus.OK);
    }
}
