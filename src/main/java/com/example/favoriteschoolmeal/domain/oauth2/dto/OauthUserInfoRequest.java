package com.example.favoriteschoolmeal.domain.oauth2.dto;


public record OauthUserInfoRequest(
        String id,
        String email,
        String nickname,
        String provider) {
}