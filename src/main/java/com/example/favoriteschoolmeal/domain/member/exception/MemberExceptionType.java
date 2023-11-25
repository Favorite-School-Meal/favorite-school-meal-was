package com.example.favoriteschoolmeal.domain.member.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum MemberExceptionType implements BaseExceptionType {

    MEMBER_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Member not found"
    ),;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    MemberExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
