package com.newworld.saegil.user.domain;

import org.springframework.http.HttpStatus;

import com.newworld.saegil.exception.CustomException;

public class InvalidUserException extends CustomException {

    public InvalidUserException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
