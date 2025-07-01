package com.newworld.saegil.file.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidFileException extends CustomException {

    public InvalidFileException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
