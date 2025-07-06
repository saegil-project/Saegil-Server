package com.newworld.saegil.file.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileProcessor {

    String upload(final MultipartFile multipartFile);

    List<String> uploadAll(final List<MultipartFile> multipartFiles);

    String getFileStorePath(final String storeName);
}
