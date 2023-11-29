package com.example.favoriteschoolmeal.domain.auth.api;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignInRequest;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpRequest;
import com.example.favoriteschoolmeal.domain.auth.service.AuthServiceImpl;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 회원 가입을 처리하는 메서드
     *
     * @param signUpRequest 회원 가입에 필요한 정보를 담은 SignUpRequest 객체
     * @return 회원 가입 결과에 대한 JwtTokenDto 객체
     */
    @PostMapping("/sign-up")
    public ApiResponse<JwtTokenDto> signUp(@RequestBody SignUpRequest signUpRequest) {
        JwtTokenDto jwtTokenDto = authService.signUp(signUpRequest);

        log.info("유저가 생성되었습니다. {}", signUpRequest.username());
        return ApiResponse.createSuccess(jwtTokenDto);
    }

    /**
     * 로그인을 처리하는 메서드
     *
     * @param signInRequest 로그인에 필요한 정보를 담은 SignInRequest 객체
     * @return 로그인 결과에 대한 JwtTokenDto 객체
     */
    @PostMapping("/sign-in")
    public ApiResponse<JwtTokenDto> signIn(@RequestBody SignInRequest signInRequest) {
        JwtTokenDto jwtTokenDto = authService.signIn(signInRequest);

        log.info("유저가 로그인 되었습니다. {}", signInRequest.username());
        return ApiResponse.createSuccess(jwtTokenDto);
    }

    /**
     * RefreshToken을 사용하여 새로운 AccessToken과 RefreshToken을 발급하는 메서드
     *
     * @param refreshToken 사용할 RefreshToken
     * @return 발급된 새로운 AccessToken과 RefreshToken에 대한 TokenDto 객체
     */
    @PostMapping("/refresh-token")
    public ApiResponse<JwtTokenDto> reCreateToken(@RequestHeader("Authorization") String refreshToken) {

        String newAccessToken = refreshTokenService.reCreateAccessTokenByRefreshToken(refreshToken);
        String newRefreshToken = refreshTokenService.reCreateRefreshTokenByRefreshToken(refreshToken);

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        refreshToken = refreshToken.substring(7);
        refreshTokenService.createRefreshToken(jwtTokenDto, jwtTokenProvider.getUsername(refreshToken));

        log.info("토큰이 재생성 되었습니다. {}", jwtTokenProvider.getUsername(refreshToken));
        return ApiResponse.createSuccess(jwtTokenDto);
    }

}
