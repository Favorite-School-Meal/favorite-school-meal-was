package com.example.favoriteschoolmeal.global.security.exception;


import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class JwtException extends BaseException {

    private final JwtExceptionType jwtExceptionType;

    public JwtException(final JwtExceptionType jwtExceptionType) {
        this.jwtExceptionType = jwtExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return jwtExceptionType;
    }
}
