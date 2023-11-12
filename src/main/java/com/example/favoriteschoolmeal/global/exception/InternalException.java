package com.example.favoriteschoolmeal.global.exception;

public class InternalException extends BaseException {

    private final InternalExceptionType internalExceptionType;

    public InternalException(final InternalExceptionType internalExceptionType) {
        this.internalExceptionType = internalExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return internalExceptionType;
    }
}

