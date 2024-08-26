package com.danzobiss.couponprocessing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.status;

@SuppressWarnings("unused")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCouponException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEntityException(InvalidCouponException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", ex.getClass().getSimpleName());
        responseBody.put("errors", ex.getErrorMessages());
        responseBody.put("message", ex.getMessage());

        return status(BAD_REQUEST).body(responseBody);
    }
}
