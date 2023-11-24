package com.example.favoriteschoolmeal.domain.oauth2.dto;

import jakarta.validation.constraints.NotBlank;

public record OauthRequest(
        @NotBlank
        OauthSignUpRequest oauthSignUpRequest,

        @NotBlank
        OauthSignInRequest oauthSignInRequest
) {


}
