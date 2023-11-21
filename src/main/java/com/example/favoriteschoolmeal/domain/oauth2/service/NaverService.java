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
    public void create(OauthUserInfoDto oauthUserInfoDto, Member member) {
    }

    @Override
    public void delete(Member member) {
    }

    @Override
    public Oauth isExists(OauthUserInfoDto oauthUserInfoDto) {
        return null;
    }

    @Override
    public String getAccessToken(OauthSignInRequest oauthSignInRequest) {
        return null;
    }
}
