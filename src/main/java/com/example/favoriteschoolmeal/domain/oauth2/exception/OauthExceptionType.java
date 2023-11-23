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
    ),

    UNSUPPORTED_ENCODING_EXCEPTION(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "State code Encoding error"
    ),

    DUPLICATE_NICKNAME_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "Nickname duplicated"
    ),

    DUPLICATE_EMAIL_EXCEPTION(
            400,
            HttpStatus.BAD_REQUEST,
            "이미 가입된 email 입니다."
    ),

    OAUTH_KAKAO_NOT_FOUND(
            400,
            HttpStatus.NOT_FOUND,
            "회원가입을 진행해야 합니다."
    ),

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
