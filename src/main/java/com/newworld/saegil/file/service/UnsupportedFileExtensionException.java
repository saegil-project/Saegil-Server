package com.newworld.saegil.file.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnsupportedFileExtensionException extends CustomException {

    public UnsupportedFileExtensionException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
