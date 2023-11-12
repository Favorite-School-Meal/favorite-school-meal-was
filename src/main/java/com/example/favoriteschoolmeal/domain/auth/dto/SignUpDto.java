package com.example.favoriteschoolmeal.domain.auth.dto;


import jakarta.validation.constraints.NotBlank;

public class SignUpDto {

    @NotBlank
    private String username;

    private String fullname;

    private String nickname;

    private String email;

    private String password;


}
