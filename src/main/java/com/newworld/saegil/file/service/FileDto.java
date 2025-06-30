package com.newworld.saegil.file.service;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record FileDto(
        Resource file,
        MediaType contentType
) {
}
