package com.example.favoriteschoolmeal.domain.oauth2.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OauthSignUpRequest(

        @NotBlank
        @Size(min = 2, max = 4, message = "이름은 2자리 이상, 4자리 이하여야 합니다.")
        String fullname,

        @NotBlank
        @Size(min = 7, max = 7, message = "문자열은 주민번호 앞자리 6자와 끝자리의 첫자를 합하여 7자리여야 합니다.")
        String personalNumber
) {

}