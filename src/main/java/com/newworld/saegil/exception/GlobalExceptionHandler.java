package com.newworld.saegil.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_MESSAGE_FORMAT = "%s : %s";

    private final Log logger = LogFactory.getLog(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleExceptionInternal(
            final Exception ex
    ) {
        logger.error(String.format(LOG_MESSAGE_FORMAT, ex.getClass().getSimpleName(), ex.getMessage()), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ExceptionResponse(ex.getMessage() == null ? "Internal Server Error" : ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(
            final IllegalArgumentException ex
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, ex.getClass().getSimpleName(), ex.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(
            final NoResourceFoundException ex
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, ex.getClass().getSimpleName(), ex.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, ex.getClass().getSimpleName(), ex.getMessage()));

        final String message = ex.getFieldErrors().stream()
                                 .map(fieldError -> fieldError.getDefaultMessage())
                                 .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(message));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(
            final CustomException ex
    ) {
        logger.warn(String.format(LOG_MESSAGE_FORMAT, ex.getClass().getSimpleName(), ex.getMessage()));

        return ResponseEntity.status(ex.getHttpStatus())
                             .body(new ExceptionResponse(ex.getMessage()));
    }
}
