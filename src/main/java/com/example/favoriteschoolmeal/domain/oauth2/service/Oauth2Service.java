package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;


public interface Oauth2Service {


    OauthUserInfoDto getUserInfo(String accessToken);

    Oauth create(OauthUserInfoDto oauthUserInfoDto, Member member);

    Oauth delete(Member member);

    Oauth isExists(Member member);

    String getAccessToken(OauthSignInRequest oauthSignInRequest);
}
