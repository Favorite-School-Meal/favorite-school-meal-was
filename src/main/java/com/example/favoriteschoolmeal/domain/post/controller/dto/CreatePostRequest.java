package com.example.favoriteschoolmeal.domain.post.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostRequest(
        @NotNull Long memberId,
        @NotNull Long matchingId,
        @NotBlank String title,
        @NotBlank String content) {

}
