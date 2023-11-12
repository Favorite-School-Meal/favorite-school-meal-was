package com.example.favoriteschoolmeal.domain.auth.service;


import com.example.favoriteschoolmeal.domain.auth.dto.JwtTokenDto;

public interface AuthService {

    JwtTokenDto signUp();
    JwtTokenDto signIn();
    void signOut();

}
