package com.example.favoriteschoolmeal.domain.auth.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignInDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpDto;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * SignUpDto를 기반으로 회원을 생성하고, 토큰을 발급하는 메서드
     * @param signUpDto 회원 가입에 필요한 정보를 담은 SignUpDto 객체
     * @return 토큰에 대한 TokenDto 객체
     * @throws  RuntimeException 이미 존재하는 ID 또는 닉네임일 경우 발생하는 예외 //예외 타입 변경해야됨
     */
    @Transactional
    @Override
    public JwtTokenDto signUp(SignUpDto signUpDto) {
        return null;
    }


    /**
     * 로그인을 처리하고, 토큰을 발급하는 메서드
     * @param signInDto 로그인에 필요한 정보를 담은 SignInDto 객체
     * @return 토큰에 대한 TokenDto 객체
     * @throws RuntimeException 존재하지 않는 유저 또는 비밀번호 불일치 시 발생하는 예외 //예외 타입 변경해야됨
     */
    @Transactional
    @Override
    public JwtTokenDto signIn(SignInDto signInDto) {
        return null;
    }

    @Override
    public void signOut() {

    }


}
