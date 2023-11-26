package com.example.favoriteschoolmeal.domain.notification.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class NotificationException extends BaseException {

    private final NotificationExceptionType notificationExceptionType;

    public NotificationException(final NotificationExceptionType notificationExceptionType) {
        this.notificationExceptionType = notificationExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return notificationExceptionType;
    }
}
