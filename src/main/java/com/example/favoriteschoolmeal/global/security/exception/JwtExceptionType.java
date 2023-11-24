package com.example.favoriteschoolmeal.global.security.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum JwtExceptionType implements BaseExceptionType {

    MALFORMED_JWT_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "잘못된 JWT 서명입니다."
    ),

    EXPIRED_JWT_EXCEPTION(
            401,
            HttpStatus.UNAUTHORIZED,
            "만료된 JWT 토큰입니다."
    ),

    UNSUPPORTED_JWT_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "지원되지 않는 JWT 토큰입니다"
    ),

    ILLEAGAL_ARGUMENT_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "JWT 토큰이 잘못되었습니다"
    ),

    REFRESHTOKEN_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "RefreshToken not found"
    ),

    INVALID_REFRESHTOKEN_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "RefreshToken invalid"
    ),
    ;

    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    JwtExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
