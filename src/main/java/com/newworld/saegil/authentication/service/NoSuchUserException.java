package com.newworld.saegil.authentication.service;

import org.springframework.http.HttpStatus;

import com.newworld.saegil.exception.CustomException;

public class NoSuchUserException extends CustomException {

    public NoSuchUserException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
