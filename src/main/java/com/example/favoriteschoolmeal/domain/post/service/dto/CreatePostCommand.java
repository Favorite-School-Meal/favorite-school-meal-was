package com.example.favoriteschoolmeal.domain.post.service.dto;

import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import jakarta.validation.constraints.NotBlank;

public record CreatePostCommand(
        Long restaurantId,
        @NotBlank String title,
        @NotBlank String content) {

    public static CreatePostCommand of(final CreatePostRequest request, final Long restaurantId) {

        return new CreatePostCommand(
                restaurantId,
                request.title(),
                request.content()
        );
    }
}
