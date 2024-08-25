package com.pawalert.backend.global.httpstatus.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 모든 예외를 처리하는 기본 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<SuccessResponse<Map<String, String>>> handleException(Exception ex, HttpServletRequest request) {
        log.error("Request: {}, Message: {}", request.getRequestURI(), ex.getLocalizedMessage(), ex);

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("exception", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getLocalizedMessage());

        return ResponseHandler.internalServerError("Internal server error", errorDetails);
    }

    // 비즈니스 로직 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<SuccessResponse<Object>> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.error("Request: {}, Message: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseHandler.generateResponse(ex.getErrorCode().getStatus(), ex.getMessage(), ex.getData());
    }

    // 유효성 검사 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SuccessResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Request: {}, Validation Error: {}", request.getRequestURI(), ex.getMessage(), ex);

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Validation Failed");
        errorDetails.put("message", errorMessage);

        return ResponseHandler.badRequest("Validation error", errorDetails);
    }

    // 요청 본문 읽기 실패 예외 처리 (잘못된 JSON 포맷 등)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<SuccessResponse<Map<String, String>>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("Request: {}, Message Not Readable: {}", request.getRequestURI(), ex.getMessage(), ex);

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Malformed JSON");
        errorDetails.put("message", ex.getMostSpecificCause().getMessage());

        return ResponseHandler.badRequest("Invalid JSON format or incorrect key/value in the request body.", errorDetails);
    }

    // 경로 변수 또는 요청 파라미터 유효성 검사 실패 예외 처리
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<SuccessResponse<Map<String, Object>>> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.error("Request: {}, Constraint Violation: {}", request.getRequestURI(), ex.getMessage(), ex);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("violations", ex.getConstraintViolations().stream()
                .map(violation -> Map.of(
                        "property", violation.getPropertyPath().toString(),
                        "message", violation.getMessage()))
                .collect(Collectors.toList()));

        return ResponseHandler.badRequest("Constraint violation occurred.", errorDetails);
    }
}
