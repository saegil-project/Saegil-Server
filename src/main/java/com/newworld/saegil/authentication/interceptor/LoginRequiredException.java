package com.newworld.saegil.authentication.interceptor;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class LoginRequiredException extends CustomException {

    public LoginRequiredException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
