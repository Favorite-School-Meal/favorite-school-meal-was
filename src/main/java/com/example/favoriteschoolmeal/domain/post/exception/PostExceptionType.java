package com.example.favoriteschoolmeal.domain.post.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum PostExceptionType implements BaseExceptionType {

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

    MATCHING_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Matching not found"
    ),

    RESTAURANT_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Restaurant not found"
    ),

    UNAUTHORIZED_ACCESS(
            401,
            HttpStatus.UNAUTHORIZED,
            "Unauthorized access"
    );

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    PostExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
