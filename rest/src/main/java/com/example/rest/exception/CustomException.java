package com.example.rest.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class CustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String customMessage;

    public CustomException(String message) {
        super(message);
        customMessage = message;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}
