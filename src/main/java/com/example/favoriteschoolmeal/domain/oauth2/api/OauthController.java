package com.example.favoriteschoolmeal.domain.oauth2.api;


import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignUpRequest;
import com.example.favoriteschoolmeal.domain.oauth2.service.OauthServiceImpl;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthServiceImpl oauthService;

    /**
     * kakao 회원가입 및 로그인을 처리하는 메서드
     *
     * @param
     * @return
     */
    @PostMapping("/sign/kakao")
    public ApiResponse<JwtTokenDto> kakaoSign(@RequestBody OauthRequest oauthRequest) {
        JwtTokenDto jwtTokenDto = oauthService.sign(oauthRequest, OauthPlatform.KAKAO);
        return ApiResponse.createSuccess(jwtTokenDto);
    }


    /**
     * naver 회원가입 및 로그인을 처리하는 메서드
     *
     * @param
     * @return
     */
    @PostMapping("/sign/naver")
    public ApiResponse<JwtTokenDto> NaverSign(@RequestBody OauthRequest oauthRequest) {
        JwtTokenDto jwtTokenDto = oauthService.sign(oauthRequest, OauthPlatform.NAVER);
        return ApiResponse.createSuccess(jwtTokenDto);
    }

    @GetMapping("/kakao/callback")
    public @ResponseBody String kakaoCallback(String code){
        return code;
    }

    @GetMapping("/naver/callback")
    public @ResponseBody String naverCallback(String code){
        return code;
    }

}
