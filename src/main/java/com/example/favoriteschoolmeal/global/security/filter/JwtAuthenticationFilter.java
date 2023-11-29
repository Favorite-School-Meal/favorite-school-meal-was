package com.example.favoriteschoolmeal.global.security.filter;


import com.example.favoriteschoolmeal.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


//실제로 이 컴포넌트를 이용하는 것은 인증 작업을 진행하는 filter
//이 필터는 검증이 끝난 JWT로 부터 유저 정보를 받아와서 UsernamePasswordAuthenticationFilter로 전달
@Order(0)
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //각 요청당 한번만 실행하기를 보장

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, UsernameNotFoundException, ServletException {

        //요청이 들어올 때마다 호출되며, JWT 토큰을 추출하고 유효성을 검사한 후, 인증 정보를 설정
        //FilterChain을 통해 다음 filter로 요청 전달
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            token = token.substring(7); //Bearer 제외

            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException e) {
                e.printStackTrace();
                //response 추가
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
