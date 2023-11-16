package com.example.favoriteschoolmeal.domain.post.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class PostException extends BaseException {

    private final PostExceptionType postExceptionType;

    public PostException(final PostExceptionType postExceptionType) {
        this.postExceptionType = postExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return postExceptionType;
    }
}
