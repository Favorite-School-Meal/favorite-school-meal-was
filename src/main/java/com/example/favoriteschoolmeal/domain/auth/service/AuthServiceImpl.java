package com.example.favoriteschoolmeal.domain.auth.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignInDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpDto;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenRepository;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * SignUpDto를 기반으로 회원을 생성하고, 토큰을 발급하는 메서드
     * @param signUpDto 회원 가입에 필요한 정보를 담은 SignUpDto 객체
     * @return 토큰에 대한 JwtTokenDto 객체
     * @throws  RuntimeException 이미 존재하는 ID 또는 닉네임일 경우 발생하는 예외 //예외 타입 변경해야됨
     */
    @Transactional
    @Override
    public JwtTokenDto signUp(SignUpDto signUpDto) {

        checkDuplication(signUpDto);

        Member member = convertSignUpDtoToMember(signUpDto);
        memberRepository.save(member);

        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername());

        JwtTokenDto jwtTokenDto = JwtTokenDto.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build();

        refreshTokenService.createRefreshToken(jwtTokenDto, member.getUsername());

        return jwtTokenDto;
    }


    /**
     * 로그인을 처리하고, 토큰을 발급하는 메서드
     * @param signInDto 로그인에 필요한 정보를 담은 SignInDto 객체
     * @return 토큰에 대한 JwtTokenDto 객체
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

    private void checkDuplication(SignUpDto signUpDto){

        if(memberRepository.findByUsername(signUpDto.username()).isPresent()){
            throw new NoSuchElementException();
        }
        if(memberRepository.findByNickname(signUpDto.nickname()).isPresent()){
            throw new NoSuchElementException();
        }
        //TODO: exception 다시 설정
    }

    /**
     * SignUpDto를 Member로 변환하는 메서드
     * @param signUpDto 변환할 SignUpDto 객체
     * @return 변환된 Member 객체
     */
    public Member convertSignUpDtoToMember(SignUpDto signUpDto) {
        final var role = Authority.ROLE_USER;
        return  Member.builder()
                .username(signUpDto.username())
                .password(passwordEncoder.encode(signUpDto.password()))
                .nickname(signUpDto.nickname())
                .fullName(signUpDto.fullname())
                .authority(role)
                .build();
    }


}
