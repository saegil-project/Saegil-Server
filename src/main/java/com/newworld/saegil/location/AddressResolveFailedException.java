package com.newworld.saegil.location;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AddressResolveFailedException extends CustomException {

    public AddressResolveFailedException(final String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
