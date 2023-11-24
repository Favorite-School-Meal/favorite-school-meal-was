package com.example.favoriteschoolmeal.domain.oauth2.exception;

import com.example.favoriteschoolmeal.global.exception.BaseException;
import com.example.favoriteschoolmeal.global.exception.BaseExceptionType;

public class OauthException extends BaseException {

    private final OauthExceptionType oauthExceptionType;

    public OauthException(final OauthExceptionType oauthExceptionType) {this.oauthExceptionType = oauthExceptionType;}

    @Override
    public BaseExceptionType exceptionType() {return  oauthExceptionType;}

}
