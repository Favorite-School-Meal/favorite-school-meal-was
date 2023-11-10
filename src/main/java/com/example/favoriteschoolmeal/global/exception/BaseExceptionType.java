package com.example.favoriteschoolmeal.global.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    int errorCode();

    HttpStatus httpStatus();

    String errorMessage();
}
