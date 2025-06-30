package com.newworld.saegil.file.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ReadFailureException extends CustomException {

    public ReadFailureException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
