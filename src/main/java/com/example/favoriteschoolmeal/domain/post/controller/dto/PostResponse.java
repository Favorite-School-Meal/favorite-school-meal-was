package com.example.favoriteschoolmeal.domain.post.controller.dto;

import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import java.time.LocalDateTime;
import java.util.Optional;

public record PostResponse(
        Long postId,
        Long writerId,
        String writer,
        String matchingStatus,
        LocalDateTime meetingDateTime,
        Integer maxParticipant,
        Long restaurantId,
        String restaurantName,
        String title,
        String content) {

    public static PostResponse from(final Post post) {
        return new PostResponse(
                post.getId(),
                post.getMember().getId(),
                post.getMember().getUsername(),
                post.getMatching().getMatchingStatus().name(),
                post.getMatching().getMeetingDateTime(),
                post.getMatching().getMaxParticipant(),
                Optional.ofNullable(post.getRestaurant()).map(Restaurant::getId).orElse(null),
                Optional.ofNullable(post.getRestaurant()).map(Restaurant::getName).orElse(null),
                post.getTitle(),
                post.getContent()
        );
    }
}
