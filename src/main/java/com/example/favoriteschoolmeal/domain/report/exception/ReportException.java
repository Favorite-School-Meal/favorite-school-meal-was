package com.example.favoriteschoolmeal.domain.report.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class ReportException extends BaseException {

    private final ReportExceptionType reportExceptionType;

    public ReportException(final ReportExceptionType reportExceptionType) {
        this.reportExceptionType = reportExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return reportExceptionType;
    }
}
