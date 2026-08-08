package com.flowforge.common.exception;

import com.flowforge.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex) {

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(), Collections.singletonList(ex.getErrorCode())

        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}
