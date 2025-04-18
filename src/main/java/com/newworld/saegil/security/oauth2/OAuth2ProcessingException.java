package com.newworld.saegil.security.oauth2;

import org.springframework.http.HttpStatus;

import com.newworld.saegil.exception.CustomException;

public class OAuth2ProcessingException extends CustomException {

    public OAuth2ProcessingException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
