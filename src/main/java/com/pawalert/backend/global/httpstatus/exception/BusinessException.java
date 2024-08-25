package com.pawalert.backend.global.httpstatus.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object data;  // 추가된 데이터 필드

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = null; // 기본적으로 데이터는 null로 설정
    }

    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.data = null;
    }

    public BusinessException(ErrorCode errorCode, String customMessage, Object data) {
        super(customMessage);
        this.errorCode = errorCode;
        this.data = data; // 데이터 필드를 설정할 수 있는 생성자
    }

    public BusinessException(ErrorCode errorCode, String customMessage, Object data, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorCode;
        this.data = data;
    }
}
