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
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * SignUpDto를 기반으로 회원을 생성하고, 토큰을 발급하는 메서드
     * @param signUpDto 회원 가입에 필요한 정보를 담은 SignUpDto 객체
     * @return 토큰에 대한 JwtTokenDto 객체
     * @throws
     */
    @Transactional
    @Override
    public JwtTokenDto signUp(SignUpDto signUpDto) {

        checkDuplication(signUpDto);

        Member member = convertSignUpDtoToMember(signUpDto);
        memberRepository.save(member);

        JwtTokenDto jwtTokenDto = creatJwtTokenDto(member);
        refreshTokenService.createRefreshToken(jwtTokenDto, member.getUsername());

        return jwtTokenDto;
    }


    /**
     * 로그인을 처리하고, 토큰을 발급하는 메서드
     * @param signInDto 로그인에 필요한 정보를 담은 SignInDto 객체
     * @return 토큰에 대한 JwtTokenDto 객체
     * @throws
     */
    @Transactional
    @Override
    public JwtTokenDto signIn(SignInDto signInDto) {

        Optional<Member> memberOptional = memberRepository.findByUsername(signInDto.username());

        // 사용자가 존재하지 않는 경우 예외 던지기
        Member member = memberOptional.orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이디 입니다."));
            //TODO: Exception 사용자가 존재하지 않습니다.
        if(!passwordEncoder.matches(signInDto.password(), member.getPassword())){
            //TODO: Exception 비밀번호가 일치하지 않습니다.
            throw new RuntimeException();
        }

        JwtTokenDto jwtTokenDto = creatJwtTokenDto(member);
        refreshTokenService.createRefreshToken(jwtTokenDto, signInDto.username());

        return jwtTokenDto;
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

    /**
     * Member username으로 JwtTokenDto를 생성하는 메서드
     * @param member
     * @return JwtTokenDto
     */
    private JwtTokenDto creatJwtTokenDto(Member member){

        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername());

       return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
