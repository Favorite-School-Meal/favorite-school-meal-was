package com.example.favoriteschoolmeal.domain.auth.api;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignInRequest;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpRequest;
import com.example.favoriteschoolmeal.domain.auth.service.AuthServiceImpl;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 회원 가입을 처리하는 메서드
     * @param signUpRequest 회원 가입에 필요한 정보를 담은 SignUpRequest 객체
     * @return 회원 가입 결과에 대한 JwtTokenDto 객체
     */
    @PostMapping("/sign-up")
    public ApiResponse<JwtTokenDto> signUp(@RequestBody SignUpRequest signUpRequest){
        JwtTokenDto jwtTokenDto = authService.signUp(signUpRequest);

        return ApiResponse.createSuccess(jwtTokenDto);
    }

    /**
     * 로그인을 처리하는 메서드
     * @param signInRequest 로그인에 필요한 정보를 담은 SignInRequest 객체
     * @return 로그인 결과에 대한 JwtTokenDto 객체
     */
    @PostMapping("/sign-in")
    public ApiResponse<JwtTokenDto> signIn(@RequestBody SignInRequest signInRequest){
        JwtTokenDto jwtTokenDto = authService.signIn(signInRequest);

        return ApiResponse.createSuccess(jwtTokenDto);
    }


}
