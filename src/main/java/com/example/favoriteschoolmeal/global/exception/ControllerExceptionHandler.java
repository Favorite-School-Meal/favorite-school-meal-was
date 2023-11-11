package com.example.favoriteschoolmeal.global.exception;

import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(BaseException.class)
    public ApiResponse<ExceptionResponse> handleBaseException(final BaseException e) {
        final BaseExceptionType baseExceptionType = e.exceptionType();
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                baseExceptionType.errorCode(), baseExceptionType.errorMessage());

        log.info("error = {}", exceptionResponse);

        return ApiResponse.createError(exceptionResponse);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            HttpMediaTypeNotSupportedException.class,
            MethodArgumentNotValidException.class})
    public ApiResponse<ExceptionResponse> handleTypeMismatchException(final Exception e) {
        final CommonExceptionType commonExceptionType = CommonExceptionType.INVALID_REQUEST_BODY;
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                commonExceptionType.errorCode(), commonExceptionType.errorMessage());

        log.info("error = {}", exceptionResponse);

        return ApiResponse.createError(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<ExceptionResponse> handleArgumentTypeMismatchException(final Exception e) {
        final CommonExceptionType commonExceptionType = CommonExceptionType.INVALID_REQUEST_PARAMETERS;
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                commonExceptionType.errorCode(), commonExceptionType.errorMessage());

        log.info("error = {}", exceptionResponse);

        return ApiResponse.createError(exceptionResponse);
    }

    @ExceptionHandler(InternalException.class)
    public ApiResponse<ExceptionResponse> handleInternalException(final InternalException e) {
        final BaseExceptionType internalExceptionType = e.exceptionType();

        log.error(e.getMessage(), e);

        final ExceptionResponse exceptionResponse = new ExceptionResponse(500, "예기치 못한 오류입니다");
        return ApiResponse.createError(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<ExceptionResponse> handleException(final Exception e) {
        log.error(e.getMessage(), e);

        final ExceptionResponse exceptionResponse = new ExceptionResponse(500, "예기치 못한 오류입니다");
        return ApiResponse.createError(exceptionResponse);
    }
}
