package com.example.favoriteschoolmeal.domain.matching.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class MatchingException extends BaseException {

    private final MatchingExceptionType matchingExceptionType;

    public MatchingException(final MatchingExceptionType matchingExceptionType) {
        this.matchingExceptionType = matchingExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return matchingExceptionType;
    }
}
