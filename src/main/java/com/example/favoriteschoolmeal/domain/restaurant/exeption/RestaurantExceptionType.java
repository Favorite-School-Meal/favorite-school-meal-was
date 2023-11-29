package com.example.favoriteschoolmeal.domain.restaurant.exeption;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum RestaurantExceptionType implements BaseExceptionType {

    RESTAURANT_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Restaurant not found"
    ), UNAUTHORIZED_ACCESS(
            401,
            HttpStatus.UNAUTHORIZED,
            "Unauthorized access"
    );

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    RestaurantExceptionType(final int errorCode, final HttpStatus httpStatus,
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
