package com.pawalert.backend.global.httpstatus.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SuccessResponse<Void>> handleException(Exception ex, HttpServletRequest request) {
        log.error("Request: {}, Message: {}", request.getRequestURI(), ex.getLocalizedMessage(), ex);
        return ResponseHandler.internalServerError("Internal server error");
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<SuccessResponse<Object>> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.error("Request: {}, Message: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseHandler.generateResponse(ex.getErrorCode().getStatus(), ex.getMessage(), ex.getData());
    }
}
