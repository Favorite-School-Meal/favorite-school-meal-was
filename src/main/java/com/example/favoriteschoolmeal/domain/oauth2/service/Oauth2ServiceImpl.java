package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.service.AuthServiceImpl;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;

import com.example.favoriteschoolmeal.domain.oauth2.repository.OauthRepository;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2ServiceImpl {

    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final AuthServiceImpl authService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final MemberRepository memberRepository;

    public OauthUserInfoDto getUserInfo(String accessToken, OauthPlatform platform) {
        return platformToService(platform).getUserInfo(accessToken);
    }

    public void create(OauthUserInfoDto oauthUserInfoDto, OauthPlatform platform, Member member) {

        platformToService(platform).create(oauthUserInfoDto, member);
    }


    public Oauth isExists(OauthUserInfoDto oauthUserInfoDto, OauthPlatform platform) {

        return platformToService(platform).isExists(oauthUserInfoDto);
    }

    public String getAccessToken(OauthSignInRequest oauthSignInRequest, OauthPlatform platform) {
        return platformToService(platform).getAccessToken(oauthSignInRequest);
    }

    public JwtTokenDto signIn(OauthSignInRequest oauthSignInRequest, OauthPlatform platform) {

        String accessToken = getAccessToken(oauthSignInRequest, platform);
        log.info("토큰이 발급되었습니다. {}", accessToken);


        OauthUserInfoDto oauthUserInfoDto = getUserInfo(accessToken, platform);
        log.info("유저 정보를 가져왔습니다. {}, {}", oauthUserInfoDto.getPlatformId(), oauthUserInfoDto.getNickname());

        Oauth existOauth = isExists(oauthUserInfoDto, platform);
        log.info("유저 정보 존재 확인. {}", existOauth);
        if (existOauth != null) { //이미 존재하는 계정이면
            //로그인

            JwtTokenDto jwtTokenDto = authService.creatJwtTokenDto(existOauth.getMember());
            refreshTokenService.createRefreshToken(jwtTokenDto, existOauth.getMember().getUsername());

            return jwtTokenDto;

        } else {
            //회원가입
            Member member = convertSignUpDtoToMember(oauthSignInRequest, oauthUserInfoDto);
            memberRepository.save(member);

            create(oauthUserInfoDto, platform, member);

            JwtTokenDto jwtTokenDto = authService.creatJwtTokenDto(member);
            refreshTokenService.createRefreshToken(jwtTokenDto, member.getUsername());

            log.info("유저가 로그인 되었습니다. {}", member.getNickname());
            return jwtTokenDto;
        }

    }


    public Member convertSignUpDtoToMember(OauthSignInRequest oauthSignInRequest, OauthUserInfoDto oauthUserInfoDto) {
        final var role = Authority.ROLE_USER;
        final var personalNumber = oauthSignInRequest.personalNumber();

        final var birthday = personalNumber.substring(0, personalNumber.length() - 1);
        final var firstNumber = personalNumber.substring(personalNumber.length() - 1);

        String randomStringUsername = generateRandomString(10);
        String randomStringPassword = generateRandomString(10);

        return Member.builder()
                .username(randomStringUsername)//TODO: 난수로 생성?
                .password(passwordEncoder.encode(randomStringPassword))
                .nickname(oauthUserInfoDto.getNickname())
                .email(oauthUserInfoDto.getEmail())
                .fullName(oauthSignInRequest.fullname())
                .authority(role)
                .age(authService.convertBirthdayToAge(birthday, firstNumber))
                .gender(authService.convertPersonalNumberToGender(firstNumber))
                .introduction(null)
                .isBanned(false)
                .build();
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


    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
