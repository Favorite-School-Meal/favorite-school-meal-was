package com.example.favoriteschoolmeal.domain.oauth2.dto;

import jakarta.validation.constraints.NotBlank;

public record OauthSignInRequest(
        @NotBlank
        String authorizeCode
) {
}
