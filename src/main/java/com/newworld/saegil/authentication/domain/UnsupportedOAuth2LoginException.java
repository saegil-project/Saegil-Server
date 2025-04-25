package com.newworld.saegil.authentication.domain;

import org.springframework.http.HttpStatus;

import com.newworld.saegil.exception.CustomException;

public class UnsupportedOAuth2LoginException extends CustomException {

    public UnsupportedOAuth2LoginException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
