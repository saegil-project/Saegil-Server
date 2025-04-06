package com.newworld.saegil.security.oauth2;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OAuth2ProcessingException extends CustomException {

    public OAuth2ProcessingException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
