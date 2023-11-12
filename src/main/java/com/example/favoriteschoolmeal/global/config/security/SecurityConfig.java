package com.example.favoriteschoolmeal.global.config.security;


import com.example.favoriteschoolmeal.global.security.handler.JwtAccessDeniedHandler;
import com.example.favoriteschoolmeal.global.security.handler.JwtAuthenticationEntryPoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtSecurityConfig jwtSecurityConfig;


    private static final String[] publicEndpoints = {

           "api/v1/auth",

            //swagger 추가
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        //csrf, cors 보안 처리 하기
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicEndpoints).permitAll()
                        .anyRequest().authenticated())



                .exceptionHandling(auth -> auth
                        .accessDeniedHandler(jwtAccessDeniedHandler) //권한 문제 발생
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)); //인증 문제 발생)


        httpSecurity.apply(jwtSecurityConfig);

        return httpSecurity.build();
    }
}
