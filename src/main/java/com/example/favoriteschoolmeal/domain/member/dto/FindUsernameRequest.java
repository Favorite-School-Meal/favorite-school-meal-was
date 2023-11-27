package com.example.favoriteschoolmeal.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record FindUsernameRequest(

        @NotBlank String fullname,
        @NotBlank String email
) {
}
