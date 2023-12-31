package com.example.favoriteschoolmeal.domain.auth.service;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignInRequest;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpRequest;
import com.example.favoriteschoolmeal.domain.auth.exception.AuthException;
import com.example.favoriteschoolmeal.domain.auth.exception.AuthExceptionType;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.exception.MemberException;
import com.example.favoriteschoolmeal.domain.member.exception.MemberExceptionType;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.model.Authority;
import com.example.favoriteschoolmeal.domain.model.Gender;
import com.example.favoriteschoolmeal.domain.oauth2.dto.OauthUserInfoDto;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthException;
import com.example.favoriteschoolmeal.domain.oauth2.exception.OauthExceptionType;
import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenServiceImpl;
import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

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

        JwtTokenDto jwtTokenDto = createJwtTokenDto(member);
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

        Member member = memberRepository.findByUsername(signInRequest.username())
                .orElseThrow(() -> new AuthException(AuthExceptionType.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(signInRequest.password(), member.getPassword())) {
            throw new AuthException(AuthExceptionType.INVALID_PASSWORD);
        }

        checkBlockOrThrow(member);

        JwtTokenDto jwtTokenDto = createJwtTokenDto(member);
        refreshTokenService.createRefreshToken(jwtTokenDto, signInRequest.username());

        return jwtTokenDto;
    }

    @Override
    public void signOut() {

    }

    private void checkDuplication(SignUpRequest signUpRequest) {

        if (memberRepository.findByUsername(signUpRequest.username()).isPresent()) {
            throw new AuthException(AuthExceptionType.DUPLICATE_USERNAME_EXCEPTION);
        }
        if (memberRepository.findByNickname(signUpRequest.nickname()).isPresent()) {
            throw new AuthException(AuthExceptionType.DUPLICATE_NICKNAME_EXCEPTION);
        }
    }

    public void checkDuplication(OauthUserInfoDto oauthUserInfoDto) {

        if (memberRepository.findByNickname(oauthUserInfoDto.getNickname()).isPresent()) {
            throw new OauthException(OauthExceptionType.DUPLICATE_NICKNAME_EXCEPTION);
        }
        if (memberRepository.findByEmail(oauthUserInfoDto.getEmail()).isPresent()) {
            throw new OauthException(OauthExceptionType.DUPLICATE_EMAIL_EXCEPTION);
        }
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
                .unblockDate(null)
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

        if (firstNumber.equals("1") || firstNumber.equals("2")) {
            birthday = birthday.minusYears(100);
        }

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
    public JwtTokenDto createJwtTokenDto(Member member) {

        String accessToken = jwtTokenProvider.createAccessToken(member.getUsername());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getUsername());

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void checkBlockOrThrow(Member member) {
        if (member.isBanned()) {
            throw new MemberException(MemberExceptionType.MEMBER_BLOCKED);
        }
    }
}
