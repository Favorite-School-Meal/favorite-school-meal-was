package com.example.favoriteschoolmeal.global.security.token.refresh;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;

public interface RefreshTokenService {

    void createRefreshToken(JwtTokenDto jwtTokenDto, String username);

    void deleteRefreshToken(JwtTokenDto jwtTokenDto, String username);

    String reCreateAccessTokenByRefreshToken(String refreshToken) throws IllegalAccessException;

    String reCreateRefreshTokenByRefreshToken(String refreshToken);
}
