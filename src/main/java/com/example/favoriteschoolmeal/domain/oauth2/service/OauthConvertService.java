package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;

import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthRequest;


import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthException;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;




@Service
@RequiredArgsConstructor
public class OauthConvertService {

    private final KakaoService kakaoService;
    private final NaverService naverService;



    public JwtTokenDto sign(OauthRequest oauthRequest, OauthPlatform platform) {

        return platformToService(platform).sign(oauthRequest);

    }

    public OauthService platformToService(OauthPlatform platform) {
        if (platform.equals(OauthPlatform.NAVER)) {
            return naverService;
        } else if (platform.equals(OauthPlatform.KAKAO)) {
            return kakaoService;
        } else throw new OauthException(OauthExceptionType.PLATFORM_BAD_REQUEST);
    }
}
