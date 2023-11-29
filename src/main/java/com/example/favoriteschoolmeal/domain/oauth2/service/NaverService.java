package com.example.favoriteschoolmeal.domain.oauth2.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.service.AuthServiceImpl;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.domain.model.Gender;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
public class NaverService implements OauthService {

    private final OauthRepository oauthRepository;
    private final AuthServiceImpl authService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;

    @Value("${oauth.naver.api-url}")
    private String apiURL;

    @Value("${oauth.naver.auth-url}")
    private String authURL;

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value(("${oauth.naver.client-secret}"))
    private String clientSecret;

    public static Integer convertBirthdayToAge(String birthdayString) {

        String birthdayDate = birthdayString.replace("-", "");

        LocalDate birthday = LocalDate.parse(birthdayDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate currentDate = LocalDate.now();

        return Period.between(birthday, currentDate).getYears();
    }

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

            authService.checkDuplication(oauthUserInfoDto);

            Member member = convertToMember(oauthUserInfoDto);
            memberRepository.save(member);

            create(oauthUserInfoDto, member);

            JwtTokenDto jwtTokenDto = authService.createJwtTokenDto(member);
            refreshTokenService.createRefreshToken(jwtTokenDto, member.getUsername());

            log.info("유저가 로그인 되었습니다. {}", member.getNickname());
            return jwtTokenDto;
        }

    }

    @Override
    public OauthUserInfoDto getUserInfo(String accessToken) {

        String postURL = apiURL + "/v1/nid/me";

        try {
            URL url = new URL(postURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

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

            JsonObject naverAccount = element.getAsJsonObject().get("response").getAsJsonObject();

            String platformId = naverAccount.getAsJsonObject().get("id").getAsString();
            String fullname = naverAccount.getAsJsonObject().get("name").getAsString();
            String nickname = naverAccount.getAsJsonObject().get("nickname").getAsString();
            String email = naverAccount.getAsJsonObject().get("email").getAsString();
            String gender = naverAccount.getAsJsonObject().get("gender").getAsString();
            String birthday = naverAccount.getAsJsonObject().get("birthyear").getAsString()
                    + naverAccount.getAsJsonObject().get("birthday").getAsString();
            String age = naverAccount.getAsJsonObject().get("age").getAsString();

            return OauthUserInfoDto.builder()
                    .platformId(platformId)
                    .fullname(fullname)
                    .nickname(nickname)
                    .email(email)
                    .birthday(birthday)
                    .gender(gender)
                    .age(age)
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
                .oauthPlatform(OauthPlatform.NAVER)
                .platformId(oauthUserInfoDto.getPlatformId())
                .nickname(oauthUserInfoDto.getNickname())
                .email(oauthUserInfoDto.getEmail())
                .build();
        oauthRepository.save(oauth);
    }

    @Override
    public void delete(Member member) {
    }

    @Override

    public Optional<Oauth> isExists(OauthUserInfoDto oauthUserInfoDto) {

        return oauthRepository.findByPlatformIdAndOauthPlatform(oauthUserInfoDto.getPlatformId(),
                OauthPlatform.NAVER);
    }

    @Override
    public String getAccessToken(OauthSignInRequest oauthSignInRequest) {

        String authorizeCode = oauthSignInRequest.authorizeCode();
        String state = oauthSignInRequest.state();
        String accessToken = "";

        String requestURL = authURL + "/oauth2.0/token";

        UriComponents uriComponents = null;
        try {
            uriComponents = UriComponentsBuilder
                    .fromUriString(requestURL)
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("code", authorizeCode)
                    .queryParam("state", URLEncoder.encode(state, "UTF-8"))
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new OauthException(OauthExceptionType.UNSUPPORTED_ENCODING_EXCEPTION);
        }

        try {
            URL url = new URL(uriComponents.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

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

        final var gender = oauthUserInfoDto.getGender().equals("M") ? Gender.MALE : Gender.FEMALE;

        final var age = convertBirthdayToAge(oauthUserInfoDto.getBirthday());

        return Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 16))
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .nickname(oauthUserInfoDto.getNickname())
                .email(oauthUserInfoDto.getEmail())
                .fullName(oauthUserInfoDto.getFullname())
                .authority(role)
                .age(age)
                .gender(gender)
                .introduction(null)
                .build();
    }


}
