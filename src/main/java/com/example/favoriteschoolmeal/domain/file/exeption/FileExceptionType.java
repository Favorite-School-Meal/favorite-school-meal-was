package com.example.favoriteschoolmeal.domain.file.exeption;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum FileExceptionType implements BaseExceptionType {

    FILE_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "File not found"
    ),
    MALFORMED_URL(
            400,
            HttpStatus.BAD_REQUEST,
            "Malformed url"
    ), FILE_IO_EXCEPTION(
            500,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "File io exception"
    ), UNSUPPORTED_EXTENSION(
            400,
            HttpStatus.BAD_REQUEST,
            "Unsupported extension"
    )
    , EMPTY_FILE_NAME(
            400,
            HttpStatus.BAD_REQUEST,
            "Empty file name"
    ),;

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    FileExceptionType(final int errorCode, final HttpStatus httpStatus,
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
