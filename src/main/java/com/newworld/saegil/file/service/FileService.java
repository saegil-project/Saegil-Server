package com.newworld.saegil.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private static final String FILE_PROTOCOL_PREFIX = "file:";

    private final FileProcessor fileProcessor;

    public FileDto read(final String storeName) throws IOException {
        final String storePath = fileProcessor.getFileStorePath(storeName);
        final Resource file = new UrlResource(FILE_PROTOCOL_PREFIX + storePath);
        validateFile(file, storeName);
        final MediaType contentType = extractContentType(file);

        return new FileDto(file, contentType);
    }

    private void validateFile(final Resource file, final String storeName) {
        if (!file.exists() || !file.isReadable()) {
            throw new ReadFailureException("파일[" + storeName + "]이 존재하지 않거나 읽을 수 없습니다.");
        }
    }

    private MediaType extractContentType(final Resource resource) throws IOException {
        final Path filePath = Paths.get(resource.getURI());
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return MediaType.parseMediaType(contentType);
    }
}
