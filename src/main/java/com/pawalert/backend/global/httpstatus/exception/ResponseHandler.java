package com.pawalert.backend.global.httpstatus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static <T> ResponseEntity<SuccessResponse<T>> generateResponse(HttpStatus status, String message, T data) {
        return new ResponseEntity<>(new SuccessResponse<>(status, message, data), status);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> ok(String message, T data) {
        return generateResponse(HttpStatus.OK, message, data);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> created(String message, T data) {
        return generateResponse(HttpStatus.CREATED, message, data);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> badRequest(String message, T data) {
        return generateResponse(HttpStatus.BAD_REQUEST, message, data);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> notFound(String message, T data) {
        return generateResponse(HttpStatus.NOT_FOUND, message, data);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> internalServerError(String message) {
        return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
    }

}
