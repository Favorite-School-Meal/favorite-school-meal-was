package com.example.favoriteschoolmeal.domain.matching.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum MatchingExceptionType implements BaseExceptionType {

    MATCHING_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Matching not found"
    ),

    MATCHING_APPLICATION_DENIED(
            403,
            HttpStatus.FORBIDDEN,
            "Matching application denied"
    ),

    POST_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Post not found"
    ),

    MEMBER_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Member not found"
    ),

    MATCHING_MEMBER_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Matching member not found"
    ),

    UNAUTHORIZED_ACCESS(
            401,
            HttpStatus.UNAUTHORIZED,
            "Unauthorized access"
    ),

    INVALID_OPERATION(
            400,
            HttpStatus.BAD_REQUEST,
            "Invalid operation"
    );

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    MatchingExceptionType(final int errorCode, final HttpStatus httpStatus,
            final String errorMessage) {
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
