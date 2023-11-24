package com.example.favoriteschoolmeal.global.security.jwt;

import com.example.favoriteschoolmeal.global.security.exception.JwtException;
import com.example.favoriteschoolmeal.global.security.exception.JwtExceptionType;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshToken;
import com.example.favoriteschoolmeal.global.security.token.refresh.RefreshTokenRepository;
import com.example.favoriteschoolmeal.global.security.userdetails.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {

    private final String secretKey;
    private final long tokenExpirationTime;
    private final String issuer;

    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    private final Key key;


    public JwtTokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.token-expiration-time}") long tokenExpirationTime,
            @Value("${jwt.issuer}") String issuer,
            UserDetailsServiceImpl userDetailsService,
            RefreshTokenRepository refreshTokenRepository) {

        this.secretKey = secretKey;
        this.tokenExpirationTime = tokenExpirationTime;
        this.issuer = issuer;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * AccessToken을 발급하는 메서드
     *
     * @param username AccessToken을 발급받을 사용자의 ID
     * @return 생성된 AccessToken 문자열
     */
    public String createAccessToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * HttpServletRequest 에서 Authorization 헤더를 추출하여 토큰을 해결하는 메서드
     *
     * @param request HttpServletRequest 객체
     * @return 추출된 토큰 문자열
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }


    /**
     * 토큰을 사용하여 클레임을 생성하고, 이를 기반으로 사용자 객체를 만들어 인증(Authentication) 객체를 반환하는 메서드
     *
     * @param token 토큰 문자열
     * @return 인증(Authentication) 객체
     */
    public Authentication getAuthentication(String token) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 주어진 토큰에서 사용자 이름을 추출하는 메서드
     *
     * @param token 추출할 사용자 이름이 포함된 토큰 문자열
     * @return 추출된 사용자 이름
     */
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 주어진 사용자 이름을 기반으로 RefreshToken을 생성하는 메서드
     *
     * @param username RefreshToken을 발급받을 사용자의 이름
     * @return 생성된 Refresh Token 문자열
     */
    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(14, ChronoUnit.DAYS)))
                .signWith(key, signatureAlgorithm)
                .compact();
    }


    /**
     * 주어진 RefreshToken을 사용하여 AccessToken을 재발급하는 메서드
     *
     * @param refreshToken AccessToken 재발급에 사용될 RefreshToken
     * @return 재발급된 AccessToken 문자열
     */
    public String reCreateAccessToken(String refreshToken) {

        String username = getUsername(refreshToken);
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUsername(username);

        if (existingToken.isPresent()) {
            String existRefreshToken = existingToken.get().getRefreshToken();


            if (!refreshToken.equals(existRefreshToken)) {
                throw new JwtException(JwtExceptionType.INVALID_REFRESHTOKEN_EXCEPTION);
            }
        } else {
            throw new JwtException(JwtExceptionType.REFRESHTOKEN_NOT_FOUND);
        }
        return createAccessToken(username);
    }


    /**
     * 주어진 토큰의 유효성을 검사하는 메서드
     *
     * @param token 검사할 토큰 문자열
     * @return 토큰의 유효성 여부 (유효한 경우 true, 그렇지 않은 경우 false)
     */
    public boolean validateToken(String token) {
        try {
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());

        } catch (MalformedJwtException e) {
            throw new JwtException(JwtExceptionType.MALFORMED_JWT_EXCEPTION);
        } catch (ExpiredJwtException e) {
            throw new JwtException(JwtExceptionType.EXPIRED_JWT_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            throw new JwtException(JwtExceptionType.UNSUPPORTED_JWT_EXCEPTION);
        } catch (IllegalArgumentException e) {
            throw new JwtException(JwtExceptionType.ILLEAGAL_ARGUMENT_EXCEPTION);
        }
    }

}
