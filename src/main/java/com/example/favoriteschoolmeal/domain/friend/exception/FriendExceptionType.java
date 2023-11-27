package com.example.favoriteschoolmeal.domain.friend.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum FriendExceptionType implements BaseExceptionType {
    UNAUTHORIZED_ACCESS(
            401,
            HttpStatus.UNAUTHORIZED,
            "Unauthorized access"
    ), MEMBER_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Member not found"
    ), ALREADY_REQUESTED(
            409,
            HttpStatus.CONFLICT,
            "Already requested"
    ), ALREADY_FRIEND(
            409,
            HttpStatus.CONFLICT,
            "Already friend"
    ), FRIEND_REQUEST_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Friend request not found"
    ), FRIEND_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Friend not found"
    );
    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    FriendExceptionType(final int errorCode, final HttpStatus httpStatus,
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
