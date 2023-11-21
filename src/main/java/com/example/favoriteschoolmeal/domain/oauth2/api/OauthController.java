package com.example.favoriteschoolmeal.domain.oauth2.api;


import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.service.Oauth2ServiceImpl;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final Oauth2ServiceImpl oauth2Service;

    /**
     * kakao 회원 가입을 처리하는 메서드
     *
     * @param
     * @return
     */
    @PostMapping("/sign-in/kakao")
    public ApiResponse<JwtTokenDto> kakaoSignIn(@RequestBody OauthSignInRequest oauthSignInRequest) {
        JwtTokenDto jwtTokenDto = oauth2Service.signIn(oauthSignInRequest, OauthPlatform.KAKAO);
        return ApiResponse.createSuccess(jwtTokenDto);
    }

    /**
     * naver 회원 가입을 처리하는 메서드
     *
     * @param
     * @return
     */
    @PostMapping("/sign-in/naver")
    public ApiResponse<JwtTokenDto> NaverSignIn(@RequestBody OauthSignInRequest oauthSignInRequest) {
        JwtTokenDto jwtTokenDto = oauth2Service.signIn(oauthSignInRequest, OauthPlatform.NAVER);
        return ApiResponse.createSuccess(jwtTokenDto);
    }
}
