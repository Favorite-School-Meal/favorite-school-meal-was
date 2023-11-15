package com.example.favoriteschoolmeal.domain.post.service.dto;

import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostCommand(
        @NotNull Long memberId,
        Long restaurantId,
        @NotBlank String title,
        @NotBlank String content) {

    public static CreatePostCommand of(final CreatePostRequest request, final Long memberId, final Long restaurantId) {
        return new CreatePostCommand(
                memberId,
                restaurantId,
                request.title(),
                request.content()
        );
    }
}
