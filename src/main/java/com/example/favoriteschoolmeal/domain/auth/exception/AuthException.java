package com.example.favoriteschoolmeal.domain.auth.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class AuthException extends BaseException {


    private AuthExceptionType authExceptionType;

    public AuthException(final AuthExceptionType authExceptionType) {
        this.authExceptionType = authExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return authExceptionType;
    }
}
