package com.example.favoriteschoolmeal.domain.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailPostRequest(
        @NotBlank
        @Email(message = "email 형식이여야 합니다.")
        String email,

        @NotBlank
        @Size(min = 3, max = 20, message = "아이디는 3자리 이상, 20자리 이하여야 합니다.")
        String username
) {
}
