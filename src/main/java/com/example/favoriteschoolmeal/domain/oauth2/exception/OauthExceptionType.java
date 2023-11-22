package com.example.favoriteschoolmeal.domain.oauth2.exception;


import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum OauthExceptionType implements BaseExceptionType {


    PLATFORM_BAD_REQUEST(
            400,
            HttpStatus.BAD_REQUEST,
            "Not supported Platform"
    ),

    MALFORMED_URL_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "Malformed URL"
    ),

    GET_ACCESSTOKEN_IO_EXCEPTION(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Get accessToken IO Exception"
    ),

    GET_USERINFO_IO_EXCEPTION(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Get user information IO Exception"
    ),

    GET_USERINFO_NULL(
            400,
            HttpStatus.BAD_REQUEST,
            "UserInformation is null"
    ),

    JSON_PARSE_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "Json parse exception"
    )
    ;




    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    OauthExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }


    @Override
    public int errorCode() {
        return 0;
    }

    @Override
    public HttpStatus httpStatus() {
        return null;
    }

    @Override
    public String errorMessage() {
        return null;
    }
}
