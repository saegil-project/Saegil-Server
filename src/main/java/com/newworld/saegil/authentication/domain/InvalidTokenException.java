package com.newworld.saegil.authentication.domain;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends CustomException {

    public InvalidTokenException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
