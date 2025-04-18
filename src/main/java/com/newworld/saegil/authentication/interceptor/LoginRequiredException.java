package com.newworld.saegil.authentication.interceptor;

import org.springframework.http.HttpStatus;

import com.newworld.saegil.exception.CustomException;

public class LoginRequiredException extends CustomException {

    public LoginRequiredException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
