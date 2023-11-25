package com.example.favoriteschoolmeal.domain.oauth2.api;


import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthRequest;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthException;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthExceptionType;
import com.example.favoriteschoolmeal.domain.oauth2.service.OauthConvertService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthConvertService oauthService;

    /**
     * kakao 회원가입 및 로그인을 처리하는 메서드
     *
     * @param
     * @return
     */
    @PostMapping("/sign/kakao")
    public ApiResponse<?> kakaoSign(@RequestBody OauthRequest oauthRequest) {

        try{
            JwtTokenDto jwtTokenDto = oauthService.sign(oauthRequest, OauthPlatform.KAKAO);
            return ApiResponse.createSuccess(jwtTokenDto);
        } catch (OauthException e){
            return ApiResponse.createError(OauthExceptionType.OAUTH_KAKAO_NOT_FOUND);
        }
    }


    /**
     * naver 회원가입 및 로그인을 처리하는 메서드
     *
     * @param
     * @return
     */
    @PostMapping("/sign/naver")
    public ApiResponse<JwtTokenDto> naverSign(@RequestBody OauthRequest oauthRequest) {
        JwtTokenDto jwtTokenDto = oauthService.sign(oauthRequest, OauthPlatform.NAVER);
        return ApiResponse.createSuccess(jwtTokenDto);
    }

    @GetMapping("/kakao/callback")
    public @ResponseBody String kakaoCallback(String code){
        return code;
    }

    @GetMapping("/naver/callback")
    public @ResponseBody String naverCallback(@RequestParam String code, @RequestParam String state) {
        return "Received code: " + code + ", state: " + state;
    }

}
