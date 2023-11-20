package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.oauth2.domain.Oauth;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthTokenDto;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoRequest;
import com.nimbusds.jose.shaded.gson.JsonElement;
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

    public JwtTokenDto signIn(OauthTokenDto oauthTokenDto){
        Oauth oauth;
        //getAccessToken으로 토큰가져오기
        //
        return null;
    }

    @Override
    public OauthUserInfoRequest getUserInfo(String accessToken) {
        return null;
    }

    @Override
    public Oauth create(OauthUserInfoRequest oauthUserInfoRequest, Member member) {
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
            connection.setDoOutput(true);


            // POST 요청에서 필요한 파라미터를 OutputStream을 통해 전송
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=REST_API_KEY 입력" + // REST_API_KEY
                    "&redirect_uri=http://localhost:8080/app/login/kakao" + // REDIRECT_URI
                    "&code=" + authorizeCode;
            bufferedWriter.write(sb);
            bufferedWriter.flush();

            int responseCode = connection.getResponseCode();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while((line = bufferedReader.readLine()) != null){
                result.append(line);
            }

            JsonElement jsonElement = JsonParser.parseString(result.toString());
            accessToken = jsonElement.getAsJsonObject().get("access_token").getAsString();
            refreshToken = jsonElement.getAsJsonObject().get("refresh_token").getAsString();

            bufferedReader.close();
            bufferedWriter.close();



        } catch (MalformedURLException e){
        //TODO: 예외처리
        } catch (IOException e){

        }

        return accessToken;
    }
}
