package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthTokenDto;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Oauth2ServiceImpl{

    private final KakaoService kakaoService;
    private final NaverService naverService;

    public OauthUserInfoRequest getUserInfo(String accessToken, OauthPlatform platform) {

        return null;
    }

    public Oauth create(OauthUserInfoRequest oauthUserInfoRequest, OauthPlatform platform, Member member) {

        return null;
    }


    public Oauth delete(Member member, OauthPlatform platform) {

        return null;
    }


    public Oauth isExists(Member member, OauthPlatform platform) {

        return null;
    }

    public String getAccessToken(){
        return null;
    }

    public JwtTokenDto signIn(OauthTokenDto oauthTokenDto, OauthPlatform platform){
        return null;
    }

    public Oauth2Service platformToService(OauthPlatform platform){
        if(platform.equals(OauthPlatform.NAVER)){
            return naverService;
        } else if (platform.equals(OauthPlatform.KAKAO)) {
            return kakaoService;
        }
        //TODO: 예외 코드 변경
        else throw new RuntimeException("지원하지 않은 소셜로그인 플랫폼입니다.");
    }
}
