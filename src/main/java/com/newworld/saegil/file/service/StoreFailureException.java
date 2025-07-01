package com.newworld.saegil.file.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class StoreFailureException extends CustomException {

    public StoreFailureException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
