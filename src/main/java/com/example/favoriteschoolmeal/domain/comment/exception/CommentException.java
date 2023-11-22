package com.example.favoriteschoolmeal.domain.comment.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;

public class CommentException extends BaseException {

    private final CommentExceptionType commentExceptionType;

    public CommentException(final CommentExceptionType commentExceptionType) {
        this.commentExceptionType = commentExceptionType;
    }

    @Override
    public CommentExceptionType exceptionType() {
        return commentExceptionType;
    }
}
