package com.example.favoriteschoolmeal.domain.email.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum EmailExceptionType implements BaseExceptionType {



    EMAIL_SEND_FAILURE(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Failed to send email"
    );


    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    EmailExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
