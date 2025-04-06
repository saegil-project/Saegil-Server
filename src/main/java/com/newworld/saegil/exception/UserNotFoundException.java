package com.newworld.saegil.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
