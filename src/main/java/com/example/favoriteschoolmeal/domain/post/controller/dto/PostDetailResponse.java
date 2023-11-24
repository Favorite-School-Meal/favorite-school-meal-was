package com.example.favoriteschoolmeal.domain.post.controller.dto;

import com.example.favoriteschoolmeal.domain.comment.controller.dto.CommentResponse;
import com.example.favoriteschoolmeal.domain.matching.controller.dto.MatchingResponse;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import java.util.List;
import java.util.Optional;

public record PostDetailResponse(
        Long postId,
        String title,
        String content,
        String createdAt,

        Long memberId,
        String username,

        Long restaurantId,
        String restaurantName,

        MatchingResponse matching,
        List<CommentResponse> comments,
        Integer commentCount) {

    public static PostDetailResponse from(final Post post, final MatchingResponse matching,
            final List<CommentResponse> comments) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt().toString(),

                post.getMember().getId(),
                post.getMember().getUsername(),

                Optional.ofNullable(post.getRestaurant()).map(Restaurant::getId).orElse(null),
                Optional.ofNullable(post.getRestaurant()).map(Restaurant::getName).orElse(null),

                matching,
                comments,
                comments.size()
        );
    }
}
