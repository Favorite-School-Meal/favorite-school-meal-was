package com.example.favoriteschoolmeal.domain.friend.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;

public class FriendException extends BaseException {

    private final FriendExceptionType friendExceptionType;

    public FriendException(final FriendExceptionType friendExceptionType) {
        this.friendExceptionType = friendExceptionType;
    }

    @Override
    public FriendExceptionType exceptionType() {
        return friendExceptionType;
    }
}
