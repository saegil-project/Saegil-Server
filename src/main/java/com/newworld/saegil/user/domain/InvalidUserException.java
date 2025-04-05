package com.newworld.saegil.user.domain;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidUserException extends CustomException {

    public InvalidUserException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
