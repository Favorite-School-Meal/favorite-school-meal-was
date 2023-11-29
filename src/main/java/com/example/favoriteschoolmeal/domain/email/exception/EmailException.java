package com.example.favoriteschoolmeal.domain.email.exception;


import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class EmailException extends BaseException {

    private final EmailExceptionType emailExceptionType;

    public EmailException(final EmailExceptionType emailExceptionType) {
        this.emailExceptionType = emailExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return emailExceptionType;
    }
}
