package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.OauthPlatform;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignInRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthSignUpRequest;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthException;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthExceptionType;

import com.example.favoriteschoolmeal.domain.oauth2.repository.OauthRepository;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.json.JsonParseException;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService implements OauthService {

    private final OauthRepository oauthRepository;

    @Value("${oauth.kakao.api-url}")
    private String apiURL;

    @Value("${oauth.kakao.auth-url}")
    private String authURL;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

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

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }


            JsonElement element = JsonParser.parseString(result.toString());

            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject(); //카카오계정 정보

            String platformId = element.getAsJsonObject().get("id").getAsString();
            String nickname = kakaoAccount.getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();


            return OauthUserInfoDto.builder()
                    .platformId(platformId)
                    .nickname(nickname)
                    .email(email)
                    .build();

        } catch (IOException e) {
            throw new OauthException(OauthExceptionType.GET_USERINFO_IO_EXCEPTION);
        } catch (NullPointerException e){
            throw new OauthException(OauthExceptionType.GET_USERINFO_NULL);
        } catch (JsonParseException e){
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

        return oauthRepository.findByPlatformIdAndOauthPlatform(oauthUserInfoDto.getPlatformId(), OauthPlatform.KAKAO);

    }

    @Override
    public String getAccessToken(OauthSignInRequest oauthSignInRequest) {


        String authorizeCode = oauthSignInRequest.authorizeCode();
        String accessToken = "";

        String requestURL = authURL + "/oauth/token";

        try {
            URL url = new URL(requestURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true); //출력 스트림을 사용하여 요청 바디를 전송할 수 있도록 설정


            // POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + clientId +
                    "&redirect_uri=http://localhost:8080/api/v1/oauth/kakao/callback" +
                    "&code=" + authorizeCode;
            //TODO: client_secret 추가
            bw.write(sb); //구성된 문자열을 출력 스트림을 통해 서버로 전송
            bw.flush();

            int responseCode = connection.getResponseCode(); //서버로부터의 응답 코드

            //응답을 읽기위한 br
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            //응답을 문자열로 변환하고, JSON으로 파싱하여 액세스 토큰과 리프레시 토큰을 추출
            JsonElement jsonElement = JsonParser.parseString(result.toString());
            accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();

            bw.close();
            br.close();


        } catch (MalformedURLException e) {
            throw new OauthException(OauthExceptionType.MALFORMED_URL_EXCEPTION);
        } catch (IOException e) {
            throw new OauthException(OauthExceptionType.GET_ACCESSTOKEN_IO_EXCEPTION);
        } catch (JsonParseException e){
            throw new OauthException(OauthExceptionType.JSON_PARSE_EXCEPTION);
        }

        return accessToken;
    }
}
