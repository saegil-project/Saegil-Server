package com.newworld.saegil.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class LocalFileProcessor implements FileProcessor {

    private static final List<String> EXTENSION = List.of("jpg", "jpeg", "png");
    private static final String EXTENSION_SEPARATOR = ".";

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.web.path}")
    private String webApiPath;

    @Override
    public String upload(final MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new InvalidFileException("파일이 비어있습니다.");
        }
        final String originalFileName = multipartFile.getOriginalFilename();
        if (originalFileName == null) {
            throw new InvalidFileException("파일 이름이 비어있습니다.");
        }
        final String storeFileName = createStoreFileName(originalFileName);
        final String uploadFullPath = this.uploadPath + storeFileName;
        upload(multipartFile, uploadFullPath);

        return webApiPath + storeFileName;
    }

    private void upload(final MultipartFile multipartFile, final String uploadPath) {
        try {
            multipartFile.transferTo(new File(uploadPath));
        } catch (IOException ex) {
            throw new StoreFailureException("파일 저장에 실패했습니다.");
        }
    }

    @Override
    public List<String> uploadAll(final List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .map(this::upload)
                .toList();
    }

    private String createStoreFileName(final String originalFilename) {
        final String extension = extractExtension(originalFilename);
        final String uuid = UUID.randomUUID().toString();

        return uuid + EXTENSION_SEPARATOR + extension;
    }

    private String extractExtension(final String originalFilename) {
        final String extension = originalFilename.substring(originalFilename.lastIndexOf(EXTENSION_SEPARATOR) + 1);
        if (!EXTENSION.contains(extension.toLowerCase())) {
            throw new UnsupportedFileExtensionException("지원하지 않는 확장자입니다. : " + extension);
        }

        return extension;
    }

    @Override
    public String getFileStorePath(final String storeName) {
        return uploadPath + storeName;
    }
}
