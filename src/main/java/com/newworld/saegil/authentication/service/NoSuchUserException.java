package com.newworld.saegil.authentication.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NoSuchUserException extends CustomException {

    public NoSuchUserException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
