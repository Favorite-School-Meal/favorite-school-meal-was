package com.example.favoriteschoolmeal.domain.auth.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignInRequest;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpRequest;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.domain.model.Gender;
import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * SignUpDto를 기반으로 회원을 생성하고, 토큰을 발급하는 메서드
     *
     * @param signUpRequest 회원 가입에 필요한 정보를 담은 SignUpDto 객체
     * @return 토큰에 대한 JwtTokenDto 객체
     * @throws
     */
    @Transactional
    @Override
    public JwtTokenDto signUp(SignUpRequest signUpRequest) {

        checkDuplication(signUpRequest);

        Member member = convertSignUpDtoToMember(signUpRequest);

        memberRepository.save(member);

        JwtTokenDto jwtTokenDto = creatJwtTokenDto(member);
        refreshTokenService.createRefreshToken(jwtTokenDto, member.getUsername());

        return jwtTokenDto;
    }


    /**
     * 로그인을 처리하고, 토큰을 발급하는 메서드
     *
     * @param signInRequest 로그인에 필요한 정보를 담은 SignInDto 객체
     * @return 토큰에 대한 JwtTokenDto 객체
     * @throws
     */
    @Transactional
    @Override
    public JwtTokenDto signIn(SignInRequest signInRequest) {

        Optional<Member> memberOptional = memberRepository.findByUsername(signInRequest.username());

        // 사용자가 존재하지 않는 경우 예외 던지기
        Member member = memberOptional.orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이디 입니다."));
        //TODO: Exception 사용자가 존재하지 않습니다.
        if (!passwordEncoder.matches(signInRequest.password(), member.getPassword())) {
            //TODO: Exception 비밀번호가 일치하지 않습니다.
            throw new RuntimeException();
        }

        JwtTokenDto jwtTokenDto = creatJwtTokenDto(member);
        refreshTokenService.createRefreshToken(jwtTokenDto, signInRequest.username());

        return jwtTokenDto;
    }

    @Override
    public void signOut() {

    }

    private void checkDuplication(SignUpRequest signUpRequest) {

        if (memberRepository.findByUsername(signUpRequest.username()).isPresent()) {
            throw new NoSuchElementException();
        }
        if (memberRepository.findByNickname(signUpRequest.nickname()).isPresent()) {
            throw new NoSuchElementException();
        }
        //TODO: exception 다시 설정
    }


    /**
     * SignUpDto를 Member로 변환하는 메서드
     *
     * @param signUpRequest 변환할 SignUpDto 객체
     * @return 변환된 Member 객체
     */
    public Member convertSignUpDtoToMember(SignUpRequest signUpRequest) {
        final var role = Authority.ROLE_USER;
        final var personalNumber = signUpRequest.personalNumber();

        final var birthday = personalNumber.substring(0, personalNumber.length() - 1);
        final var firstNumber = personalNumber.substring(personalNumber.length() - 1);

        return Member.builder()
                .username(signUpRequest.username())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .nickname(signUpRequest.nickname())
                .email(signUpRequest.email())
                .fullName(signUpRequest.fullname())
                .authority(role)
                .age(convertBirthdayToAge(birthday, firstNumber))
                .gender(convertPersonalNumberToGender(firstNumber))
                .introduction(null)
                .isBanned(false)
                .build();
    }

    /**
     * Member 주민번호로 나이를 유도하는 메서드
     *
     * @param birthdayString String
     * @return Gender
     */
    public Integer convertBirthdayToAge(String birthdayString, String firstNumber) {

        LocalDate birthday = LocalDate.parse(birthdayString, DateTimeFormatter.ofPattern("yyMMdd"));


        if (firstNumber.equals("1") || firstNumber.equals("2"))
            birthday = birthday.minusYears(100);

        LocalDate currentDate = LocalDate.now();

        return Period.between(birthday, currentDate).getYears();
    }

    /**
     * Member 주민번호로 성별을 유도하는 메서드
     *
     * @param firstNumber String
     * @return Gender
     */
    public Gender convertPersonalNumberToGender(String firstNumber) {

        if (firstNumber.equals("1") || firstNumber.equals("3")) {
            return Gender.MALE;
        } else {
            return Gender.FEMALE;
        }
    }

    /**
     * Member username으로 JwtTokenDto를 생성하는 메서드
     *
     * @param member
     * @return JwtTokenDto
     */
    public JwtTokenDto creatJwtTokenDto(Member member) {

        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername());

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
