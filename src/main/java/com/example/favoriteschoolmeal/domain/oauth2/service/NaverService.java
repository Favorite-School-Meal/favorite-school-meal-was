package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthTokenDto;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverService implements Oauth2Service {

    @Override
    public OauthUserInfoRequest getUserInfo(String accessToken) {
        return null;
    }

    @Override
    public Oauth create(OauthUserInfoRequest oauthUserInfoRequest, Member member) {
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
    public String getAccessToken(String authorizeCode) {
        return null;
    }
}
