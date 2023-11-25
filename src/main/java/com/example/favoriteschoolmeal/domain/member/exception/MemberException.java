package com.example.favoriteschoolmeal.domain.member.exception;


import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class MemberException extends BaseException {

    private final MemberExceptionType memberExceptionType;

    public MemberException(final MemberExceptionType memberExceptionType) {this.memberExceptionType = memberExceptionType;}

    @Override
    public BaseExceptionType exceptionType() {return  memberExceptionType;}
}
