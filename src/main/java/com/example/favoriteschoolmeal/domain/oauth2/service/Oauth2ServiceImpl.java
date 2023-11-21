package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Oauth2ServiceImpl {

    private final KakaoService kakaoService;
    private final NaverService naverService;

    public OauthUserInfoDto getUserInfo(String accessToken, OauthPlatform platform) {
        return platformToService(platform).getUserInfo(accessToken);
    }

    public Oauth create(OauthUserInfoDto oauthUserInfoDto, OauthPlatform platform, Member member) {

        return null;
    }


    public Oauth delete(Member member, OauthPlatform platform) {

        return null;
    }


    public Oauth isExists(Member member, OauthPlatform platform) {

        return null;
    }

    public String getAccessToken(OauthSignInRequest oauthSignInRequest, OauthPlatform platform) {
        return platformToService(platform).getAccessToken(oauthSignInRequest);
    }

    public JwtTokenDto signIn(OauthSignInRequest oauthSignInRequest, OauthPlatform platform) {

        String accessToken = getAccessToken(oauthSignInRequest, platform);
        OauthUserInfoDto oauthUserInfoDto = getUserInfo(accessToken, platform);

        return null;
    }

    public JwtTokenDto signIn(OauthSignInRequest oauthSignInRequest) {

//        String accessToken = getAccessToken(authorizeCode);
//        OauthUserInfoDto oauthUserInfoDto = getUserInfo(accessToken);
//
//        //사용자가 존재할 경우에는 로그인
//        //없으면 회원가입하고 로그인
//        //닉네임, 이메일은 가져올수있지만 아이디,패스워드는 난수로 만들어서 저장?,  !실명,주민번호를 가져와야한다.
//
//        Member member = Member.builder().build();
//        Oauth oauth = create(oauthUserInfoDto, member);
//        JwtTokenDto jwtTokenDto = authService.creatJwtTokenDto();
//        refreshTokenService.createRefreshToken(jwtTokenDto,);
//
//        return jwtTokenDto;
        return null;
    }

    public Oauth2Service platformToService(OauthPlatform platform) {
        if (platform.equals(OauthPlatform.NAVER)) {
            return naverService;
        } else if (platform.equals(OauthPlatform.KAKAO)) {
            return kakaoService;
        }
        //TODO: 예외 코드 변경
        else throw new RuntimeException("지원하지 않은 소셜로그인 플랫폼입니다.");
    }
}
