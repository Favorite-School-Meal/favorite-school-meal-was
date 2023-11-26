package com.example.favoriteschoolmeal.domain.notification.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum NotificationExceptionType implements BaseExceptionType {

    NOTIFICATION_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Notification not found"
    ),

    MEMBER_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Member not found"
    ),

    UNAUTHORIZED_ACCESS(
            401,
            HttpStatus.UNAUTHORIZED,
            "Unauthorized access"
    );

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    NotificationExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
