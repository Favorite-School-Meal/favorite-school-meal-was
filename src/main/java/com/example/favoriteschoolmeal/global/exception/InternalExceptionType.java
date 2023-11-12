package com.example.favoriteschoolmeal.global.exception;

import org.springframework.http.HttpStatus;

public enum InternalExceptionType implements BaseExceptionType {

    FAIL_OBJECT_TO_JSON(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "객체를 JSON으로 변환하지 못했습니다."
    ),

    FAIL_JSON_TO_OBJECT(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "JSON를 객체로 변환하지 못했습니다."
    ),
    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    InternalExceptionType(final int errorCode,
            final HttpStatus httpStatus,
            final String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int errorCode() {
        return errorCode;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
