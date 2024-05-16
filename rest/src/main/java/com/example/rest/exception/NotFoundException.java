package com.example.rest.exception;

import java.io.Serial;

public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String customMessage;

    public NotFoundException(String message) {
        super(message);
        customMessage = message;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}
