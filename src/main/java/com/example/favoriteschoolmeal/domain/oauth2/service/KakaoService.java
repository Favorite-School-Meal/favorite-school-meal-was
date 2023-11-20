package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthTokenDto;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoService implements Oauth2Service {

    public JwtTokenDto signIn(String authorizeCode){

        String accessToken = getAccessToken(authorizeCode);
        OauthUserInfoDto oauthUserInfoDto = getUserInfo(accessToken);

        //사용자가 존재할 경우에는 로그인
        //없으면 회원가입하고 로그인

        return null;
    }

    @Override
    public OauthUserInfoDto getUserInfo(String accessToken) {

        String postURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(postURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }


            JsonElement element = JsonParser.parseString(result.toString());

            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject(); //카카오계정 정보


            String fullname = kakaoAccount.getAsJsonObject().get("name").getAsString();
            String nickname = kakaoAccount.getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            String birthday = kakaoAccount.getAsJsonObject().get("birthyear").getAsString() + kakaoAccount.getAsJsonObject().get("birthday").getAsString(); //YYYYMMDD
            String gender = kakaoAccount.getAsJsonObject().get("gender").getAsString(); //female, male


            return OauthUserInfoDto.builder()
                    .fullname(fullname)
                    .nickname(nickname)
                    .email(email)
                    .birthday(birthday)
                    .gender(gender)
                    .build();

        } catch (IOException exception) {
            return null; //TODO: 예외처리
        }
    }

    @Override
    public Oauth create(OauthUserInfoDto oauthUserInfoDto, Member member) {
        return null;
    }

    @Override
    public Oauth delete(Member member) {
        return null;
    }

    @Override
    public Oauth isExists(Member member) {
        return null;
    }

    @Override
    public String getAccessToken(String authorizeCode) {

        String accessToken = "";
        String refreshToken = "";

        String requestURL = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(requestURL);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true); //출력 스트림을 사용하여 요청 바디를 전송할 수 있도록 설정


            // POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=REST_API_KEY 입력" + // TODO: 앱 REST API 키
                    "&redirect_uri=http://localhost:8080/app/login/kakao" + // TODO: 인가 코드가 리다이렉트된 URI
                    "&code=" + authorizeCode;
            //TODO: client_secret 추가
            bw.write(sb); //구성된 문자열을 출력 스트림을 통해 서버로 전송
            bw.flush();

            int responseCode = connection.getResponseCode(); //서버로부터의 응답 코드

            //응답을 읽기위한 br
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while((line = br.readLine()) != null){
                result.append(line);
            }

            //응답을 문자열로 변환하고, JSON으로 파싱하여 액세스 토큰과 리프레시 토큰을 추출
            JsonElement jsonElement = JsonParser.parseString(result.toString());
            accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();
            refreshToken = jsonElement.getAsJsonObject().get("refresh_token").getAsString();

            bw.close();
            br.close();



        } catch (MalformedURLException e){
        //TODO: 예외처리
        } catch (IOException e){

        }

        return accessToken;
    }
}
