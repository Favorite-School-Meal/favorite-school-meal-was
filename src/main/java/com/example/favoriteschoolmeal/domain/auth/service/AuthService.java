package com.example.favoriteschoolmeal.domain.auth.service;


import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignInDto;
import com.example.favoriteschoolmeal.domain.auth.dto.SignUpDto;

public interface AuthService {

    JwtTokenDto signUp(SignUpDto signupDto);
    JwtTokenDto signIn(SignInDto signInDto);
    void signOut();

}
