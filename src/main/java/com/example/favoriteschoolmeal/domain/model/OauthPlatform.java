package com.example.favoriteschoolmeal.domain.model;

public enum OauthPlatform {

    KAKAO("KAKAO", 0),
    NAVER("NAVER", 1);

    private String platform;
    private int code;

    OauthPlatform(String platform, int code){
        this.platform = platform;
        this.code = code;
    }

}
