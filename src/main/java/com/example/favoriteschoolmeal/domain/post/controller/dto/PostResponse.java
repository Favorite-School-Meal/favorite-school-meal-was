package com.example.favoriteschoolmeal.domain.post.controller.dto;

import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

public record PostResponse(
        @NotNull Long id,
        @NotNull Long memberId,
        @NotNull Long matchingId,
        Long restaurantId,
        @NotBlank String title,
        @NotBlank String content) {

    public static PostResponse from(final Post post) {
        return new PostResponse(
                post.getId(),
                post.getMember().getId(),
                post.getMatching().getId(),
                Optional.ofNullable(post.getRestaurant()).map(Restaurant::getId).orElse(null),
                post.getTitle(),
                post.getContent()
        );
    }
}
