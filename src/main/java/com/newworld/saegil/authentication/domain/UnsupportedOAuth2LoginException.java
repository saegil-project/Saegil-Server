package com.newworld.saegil.authentication.domain;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnsupportedOAuth2LoginException extends CustomException {

    public UnsupportedOAuth2LoginException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
