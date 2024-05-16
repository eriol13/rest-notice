package com.example.rest.exception.dto;

public record ExceptionResponseDTO(
        int status, String message
) {
}
