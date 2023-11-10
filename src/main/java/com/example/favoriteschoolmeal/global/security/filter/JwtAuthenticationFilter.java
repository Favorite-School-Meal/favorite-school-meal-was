package com.example.favoriteschoolmeal.global.security.filter;


import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//실제로 이 컴포넌트를 이용하는 것은 인증 작업을 진행하는 filter
//이 필터는 검증이 끝난 JWT로 부터 유저 정보를 받아와서 UsernamePasswordAuthenticationFilter로 전달
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //각 요청당 한번만 실행하기를 보장

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, SecurityException{

        String token = jwtTokenProvider.resolveToken(request);
    }

}
