package com.example.favoriteschoolmeal.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FindUsernameRequest(

        @NotBlank
        @Size(min = 2, max = 4)
        String fullname,

        @NotBlank
        @Email(message = "email 형식이여야 합니다.")
        String email
) {

}
