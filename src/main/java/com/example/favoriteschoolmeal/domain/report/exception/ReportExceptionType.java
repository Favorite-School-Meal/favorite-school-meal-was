package com.example.favoriteschoolmeal.domain.report.exception;

import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ReportExceptionType implements BaseExceptionType {

    MEMBER_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Member not found"
    ),
    POST_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Post not found"
    ),
    COMMENT_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Comment not found"
    ),
    CHAT_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Chat not found"
    ),
    REPORT_TYPE_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Report type not found"
    ),
    UNAUTHORIZED_ACCESS(
            401,
            HttpStatus.UNAUTHORIZED,
            "Unauthorized access"
    ),
    REPORT_NOT_FOUND(
            404,
            HttpStatus.NOT_FOUND,
            "Report not found"
    ),
    ALREADY_RESOLVED(
            400,
            HttpStatus.BAD_REQUEST,
            "Already resolved"
    );
    private final int errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    ReportExceptionType(final int errorCode, final HttpStatus httpStatus, final String errorMessage) {
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
