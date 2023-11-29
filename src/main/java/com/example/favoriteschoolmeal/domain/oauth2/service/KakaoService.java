package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.service.AuthServiceImpl;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthException;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthExceptionType;
import com.example.favoriteschoolmeal.domain.oauth2.repository.OauthRepository;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenService;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService implements OauthService {

    private final OauthRepository oauthRepository;
    private final AuthServiceImpl authService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;

    @Value("${oauth.kakao.api-url}")
    private String apiURL;

    @Value("${oauth.kakao.auth-url}")
    private String authURL;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Override
    public JwtTokenDto sign(OauthRequest oauthRequest) {
        String accessToken = getAccessToken(oauthRequest.oauthSignInRequest());

        OauthUserInfoDto oauthUserInfoDto = getUserInfo(accessToken);

        Optional<Oauth> existOauth = isExists(oauthUserInfoDto);

        if (existOauth.isPresent()) {

            authService.checkBlockOrThrow(existOauth.get().getMember());

            JwtTokenDto jwtTokenDto = authService.createJwtTokenDto(existOauth.get().getMember());
            refreshTokenService.createRefreshToken(jwtTokenDto,
                    existOauth.get().getMember().getUsername());

            return jwtTokenDto;

        } else {

            if (!oauthRequest.oauthSignUpRequest().fullname().isEmpty()
                    && !oauthRequest.oauthSignUpRequest().personalNumber().isEmpty()) {

                OauthUserInfoDto newOauthUserInfoDto = OauthUserInfoDto.builder()
                        .platformId(oauthUserInfoDto.getPlatformId())
                        .fullname(oauthRequest.oauthSignUpRequest().fullname())
                        .personalNumber(oauthRequest.oauthSignUpRequest().personalNumber())
                        .nickname(oauthUserInfoDto.getNickname())
                        .email(oauthUserInfoDto.getEmail())
                        .build();

                authService.checkDuplication(newOauthUserInfoDto);

                Member member = convertToMember(newOauthUserInfoDto);
                memberRepository.save(member);

                create(oauthUserInfoDto, member);

                JwtTokenDto jwtTokenDto = authService.createJwtTokenDto(member);
                refreshTokenService.createRefreshToken(jwtTokenDto, member.getUsername());

                log.info("유저가 로그인 되었습니다. {}", member.getNickname());
                return jwtTokenDto;
            }
            throw new OauthException(OauthExceptionType.OAUTH_KAKAO_NOT_FOUND);
        }
    }

    @Override
    public OauthUserInfoDto getUserInfo(String accessToken) {

        String postURL = apiURL + "/v2/user/me";

        try {
            URL url = new URL(postURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            //http 응답요청 코드 성공:200
            int responseCode = connection.getResponseCode();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonElement element = JsonParser.parseString(result.toString());

            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account")
                    .getAsJsonObject(); //카카오계정 정보

            String platformId = element.getAsJsonObject().get("id").getAsString();
            String nickname = kakaoAccount.getAsJsonObject().get("profile").getAsJsonObject()
                    .get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();

            return OauthUserInfoDto.builder()
                    .platformId(platformId)
                    .nickname(nickname)
                    .email(email)
                    .build();

        } catch (IOException e) {
            throw new OauthException(OauthExceptionType.GET_USERINFO_IO_EXCEPTION);
        } catch (NullPointerException e) {
            throw new OauthException(OauthExceptionType.GET_USERINFO_NULL);
        } catch (JsonParseException e) {
            throw new OauthException(OauthExceptionType.JSON_PARSE_EXCEPTION);

        }
    }

    @Override
    public void create(OauthUserInfoDto oauthUserInfoDto, Member member) {

        Oauth oauth = Oauth.builder()
                .member(member)
                .oauthPlatform(OauthPlatform.KAKAO)
                .platformId(oauthUserInfoDto.getPlatformId())
                .nickname(oauthUserInfoDto.getNickname())
                .email(oauthUserInfoDto.getEmail())
                .build();
        oauthRepository.save(oauth);
    }

    @Override
    public void delete(Member member) {
        //TODO: Oauth 계정 삭제

    }

    @Override
    public Optional<Oauth> isExists(OauthUserInfoDto oauthUserInfoDto) {

        return oauthRepository.findByPlatformIdAndOauthPlatform(oauthUserInfoDto.getPlatformId(),
                OauthPlatform.KAKAO);

    }

    @Override
    public String getAccessToken(OauthSignInRequest oauthSignInRequest) {

        String authorizeCode = oauthSignInRequest.authorizeCode();
        String accessToken = "";

        String requestURL = authURL + "/oauth/token";
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(requestURL)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", "http://localhost:8080/api/v1/oauth/kakao/callback")
                .queryParam("code", authorizeCode)
                .build();

        try {

            URL url = new URL(uriComponents.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode(); //서버로부터의 응답 코드

            //응답을 읽기위한 br
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            //응답을 문자열로 변환하고, JSON으로 파싱하여 액세스 토큰과 리프레시 토큰을 추출
            JsonElement jsonElement = JsonParser.parseString(result.toString());
            accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();

            br.close();


        } catch (MalformedURLException e) {
            throw new OauthException(OauthExceptionType.MALFORMED_URL_EXCEPTION);
        } catch (IOException e) {
            throw new OauthException(OauthExceptionType.GET_ACCESSTOKEN_IO_EXCEPTION);
        } catch (JsonParseException e) {
            throw new OauthException(OauthExceptionType.JSON_PARSE_EXCEPTION);
        }

        return accessToken;
    }

    @Override
    public Member convertToMember(OauthUserInfoDto oauthUserInfoDto) {
        final var role = Authority.ROLE_USER;
        final var personalNumber = oauthUserInfoDto.getPersonalNumber();

        final var birthday = personalNumber.substring(0, personalNumber.length() - 1);
        final var firstNumber = personalNumber.substring(personalNumber.length() - 1);

        return Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 16))
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .nickname(oauthUserInfoDto.getNickname())
                .email(oauthUserInfoDto.getEmail())
                .fullName(oauthUserInfoDto.getFullname())
                .authority(role)
                .age(authService.convertBirthdayToAge(birthday, firstNumber))
                .gender(authService.convertPersonalNumberToGender(firstNumber))
                .introduction(null)
                .build();
    }
}
