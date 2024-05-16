package com.example.rest.exception;

import com.example.rest.exception.dto.ExceptionResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler({
            CustomException.class
    })
    public ResponseEntity<ExceptionResponseDTO> customExceptionHandler(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.error("Custom Exception", ex);

        String message = "잘못된 요청입니다.";

        if(ex instanceof CustomException) {
            message = ((CustomException) ex).getCustomMessage();
        }

        return new ResponseEntity<>(new ExceptionResponseDTO(HttpStatus.BAD_REQUEST.value(), message), headers, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ExceptionResponseDTO> notFoundExceptionHandler(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.error("NotFound Exception", ex);

        String message = "요청하신 정보를 찾을 수 없습니다.";

        if(ex instanceof NotFoundException) {
            message = ((NotFoundException) ex).getCustomMessage();
        }

        return new ResponseEntity<>(new ExceptionResponseDTO(HttpStatus.NOT_FOUND.value(), message), headers, HttpStatus.NOT_FOUND);
    }


}
