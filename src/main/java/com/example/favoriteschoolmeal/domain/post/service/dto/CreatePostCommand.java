package com.example.favoriteschoolmeal.domain.post.service.dto;

import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import java.time.LocalDateTime;

public record CreatePostCommand(
        Long restaurantId,
        String title,
        String content,
        LocalDateTime meetingDateTime,
        Integer maxParticipant) {

    public static CreatePostCommand of(final CreatePostRequest request, final Long restaurantId) {

        return new CreatePostCommand(
                restaurantId,
                request.title(),
                request.content(),
                request.meetingDateTime(),
                request.maxParticipant()
        );
    }
}
