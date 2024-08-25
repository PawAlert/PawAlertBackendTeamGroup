package com.pawalert.backend.global.httpstatus.exception;

import org.springframework.http.HttpStatus;

public record SuccessResponse<T>(
        HttpStatus status,
        String message,
        T data
) {
}
