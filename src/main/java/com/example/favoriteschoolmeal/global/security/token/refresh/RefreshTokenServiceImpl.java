package com.example.favoriteschoolmeal.global.security.token.refresh;

import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * RefreshToken을 새로 발행는 메서드
     * @param
     * @return void
     */
    @Transactional
    @Override
    public void createRefreshToken(JwtTokenDto jwtTokenDto, String username){

        RefreshToken refreshToken = RefreshToken.builder()
                        .refreshToken(jwtTokenDto.getRefreshToken())
                        .username(username)
                        .build();

        if(refreshTokenRepository.findByUsername(username).isPresent()){
            refreshTokenRepository.deleteByUsername(username);
        }
        refreshTokenRepository.save(refreshToken);
        log.info("create new refresh token. username : {}", username);
    }

    /**
     * RefreshToken을 삭제하는 메서드
     * @param
     * @return void
     */
    @Override
    public void deleteRefreshToken(JwtTokenDto jwtTokenDto, String username){

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(username);

        if(refreshToken.isPresent()){
            refreshTokenRepository.delete(refreshToken.get());
            log.info("delete refresh token. username : {}", username);
        } else {
            throw new NoSuchElementException("Refresh token not found for username: " + username);
        }

    }



    /**
     * 주어진 RefreshToken을 사용하여 새로운 AccessToken을 재발급하는 메서드
     * @param refreshToken 재발급에 사용될 RefreshToken
     * @return 재발급된 AccessToken 문자열
     */
    @Override
    public String reCreateAccessTokenByRefreshToken(String refreshToken) throws IllegalAccessException {
        if(jwtTokenProvider.validateToken(refreshToken)){
            refreshToken = refreshToken.substring(7);
            return jwtTokenProvider.reCreateAccessToken(refreshToken);
        }
        return null;
    }


    /**
     * 주어진 RefreshToken을 사용하여 새로운 RefreshToken을 발급하는 메서드
     * @param refreshToken 재발급에 사용될 RefreshToken
     * @return 재발급된 RefreshToken 문자열
     */
    @Override
    public String reCreateRefreshTokenByRefreshToken(String refreshToken) {
        if(jwtTokenProvider.validateToken(refreshToken)){
            refreshToken = refreshToken.substring(7);
            return jwtTokenProvider.createRefreshToken(jwtTokenProvider.getUsername(refreshToken));
        }
        return null;
    }
}
