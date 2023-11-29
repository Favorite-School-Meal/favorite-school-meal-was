package com.example.favoriteschoolmeal.domain.auth.service;


import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignInRequest;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpRequest;

public interface AuthService {

    JwtTokenDto signUp(SignUpRequest signupRequest);

    JwtTokenDto signIn(SignInRequest signInRequest);

    void signOut();

}
