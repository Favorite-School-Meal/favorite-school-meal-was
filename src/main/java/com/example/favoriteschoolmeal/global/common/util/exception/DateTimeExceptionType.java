package com.example.favoriteschoolmeal.global.common.util.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum DateTimeExceptionType implements BaseExceptionType {

    INVALID_DATE_TIME_RANGE(
            400,
            HttpStatus.BAD_REQUEST,
            "Invalid date time range"
    ),

    PARSING_ERROR(
            400,
            HttpStatus.BAD_REQUEST,
            "Parsing error"
    );

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    DateTimeExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
