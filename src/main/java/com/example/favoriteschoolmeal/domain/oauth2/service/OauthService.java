package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;

import java.util.Optional;


public interface OauthService {


    OauthUserInfoDto getUserInfo(String accessToken);

    void create(OauthUserInfoDto oauthUserInfoDto, Member member);

    void delete(Member member);

    Optional<Oauth> isExists(OauthUserInfoDto oauthUserInfoDto);

    String getAccessToken(OauthSignInRequest oauthSignInRequest);
}
