package com.example.favoriteschoolmeal.domain.post.controller.dto;

import com.example.favoriteschoolmeal.domain.matching.controller.dto.MatchingResponse;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import java.util.Optional;

public record PostSummaryResponse(
        Long postId,
        String title,
        String content,
        String createdAt,

        Long memberId,
        String username,

        Long restaurantId,
        String restaurantName,

        MatchingResponse matching,

        Integer commentCount) {

    public static PostSummaryResponse from(final Post post, final MatchingResponse matching, final Integer commentCount) {
        return new PostSummaryResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt().toString(),

                post.getMember().getId(),
                post.getMember().getUsername(),

                Optional.ofNullable(post.getRestaurant()).map(Restaurant::getId).orElse(null),
                Optional.ofNullable(post.getRestaurant()).map(Restaurant::getName).orElse(null),

                matching,

                commentCount
        );
    }
}
