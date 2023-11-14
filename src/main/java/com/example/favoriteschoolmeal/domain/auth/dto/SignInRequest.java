package com.example.favoriteschoolmeal.domain.auth.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInRequest(

        @NotBlank
        String username,

        @NotBlank
        @Size(min = 8, max = 16, message = "비밀번호의 길이는 8 ~ 16자입니다")
        String password
){

}
