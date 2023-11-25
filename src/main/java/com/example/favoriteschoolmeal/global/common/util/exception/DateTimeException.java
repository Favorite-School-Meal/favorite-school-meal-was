package com.example.favoriteschoolmeal.global.common.util.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class DateTimeException extends BaseException {

    private final DateTimeExceptionType dateTimeExceptionType;

    public DateTimeException(final DateTimeExceptionType dateTimeExceptionType) {
        this.dateTimeExceptionType = dateTimeExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return dateTimeExceptionType;
    }

}
