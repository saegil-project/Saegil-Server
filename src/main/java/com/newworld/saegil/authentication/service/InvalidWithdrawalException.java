package com.newworld.saegil.authentication.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidWithdrawalException extends CustomException {

    public InvalidWithdrawalException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
