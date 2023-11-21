package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverService implements Oauth2Service {


    @Override
    public OauthUserInfoDto getUserInfo(String accessToken) {
        return null;
    }

    @Override
    public Oauth create(OauthUserInfoDto oauthUserInfoDto, Member member) {
        return null;
    }

    @Override
    public Oauth delete(Member member) {
        return null;
    }

    @Override
    public Oauth isExists(Member member) {
        return null;
    }

    @Override
    public String getAccessToken(OauthSignInRequest oauthSignInRequest) {
        return null;
    }
}
