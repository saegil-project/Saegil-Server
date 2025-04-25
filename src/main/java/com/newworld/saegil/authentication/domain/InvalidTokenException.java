package com.newworld.saegil.authentication.domain;

import org.springframework.http.HttpStatus;

import com.newworld.saegil.exception.CustomException;

public class InvalidTokenException extends CustomException {

    public InvalidTokenException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
