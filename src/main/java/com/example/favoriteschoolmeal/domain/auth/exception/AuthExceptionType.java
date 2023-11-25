package com.example.favoriteschoolmeal.domain.auth.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum AuthExceptionType implements BaseExceptionType {

    MEMBER_NOT_FOUND(
            400,
            HttpStatus.BAD_REQUEST,
            "Member not found"
    ),

    INVALID_PASSWORD(
            400,
            HttpStatus.BAD_REQUEST,
            "Invalid password"
    ),

    DUPLICATE_NICKNAME_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "Nickname duplicated"
    ),

    DUPLICATE_USERNAME_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "이미 가입된 email 입니다."
    ),

    ;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    AuthExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
