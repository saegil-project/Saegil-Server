package com.newworld.saegil.news.service;

import com.newworld.saegil.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NewsQuizNotFoundException extends CustomException {

    public NewsQuizNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
