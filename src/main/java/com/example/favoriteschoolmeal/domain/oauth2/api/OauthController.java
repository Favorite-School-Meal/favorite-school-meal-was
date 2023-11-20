package com.example.favoriteschoolmeal.domain.oauth2.api;


import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpRequest;
import com.example.favoriteschoolmeal.domain.oauth2.service.KakaoService;
import com.example.favoriteschoolmeal.domain.oauth2.service.NaverService;
import com.example.favoriteschoolmeal.domain.oauth2.service.Oauth2Service;
import com.example.favoriteschoolmeal.domain.oauth2.service.Oauth2ServiceImpl;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import com.example.favoriteschoolmeal.global.exception.BaseException;
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
    public ApiResponse<JwtTokenDto> kakaoSignIn(@RequestParam String authorizeCode) {
        //인가코드로 token발급 요청
        //token으로 플랫폼 서버에 유저정보 요청 후 받아오기
        //db에 해당 유저가 없으면 해당 정보로 회원가입

        //signIn()

       return null;
    }

    /**
     * naver 회원 가입을 처리하는 메서드
     *
     * @param
     * @return
     */
    @PostMapping("/sign-in/naver")
    public ApiResponse<JwtTokenDto> NaverSignIn(@RequestParam String authorizeCode) {
        return null;
    }
}
