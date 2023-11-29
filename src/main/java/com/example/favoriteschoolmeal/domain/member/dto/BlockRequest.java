package com.example.favoriteschoolmeal.domain.member.dto;

import jakarta.validation.constraints.NotNull;

public record BlockRequest(
        @NotNull
        Long blockHours
) {
}
