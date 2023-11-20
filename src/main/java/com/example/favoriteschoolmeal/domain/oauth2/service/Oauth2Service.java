package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoRequest;

public interface Oauth2Service {

    OauthUserInfoRequest getUserInfo(OauthSignInRequest oauthSignInRequest);

    Oauth create(OauthUserInfoRequest oauthUserInfoRequest, Member member);
    Oauth delete(Member member);
    Oauth isExists(Member member);

}
