package com.example.favoriteschoolmeal.domain.post.service.dto;

import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostCommand(
        @NotNull Long memberId,
        @NotNull Long matchingId,
        @NotBlank String title,
        @NotBlank String content) {

    public static CreatePostCommand from(final CreatePostRequest createPostRequest) {
        return new CreatePostCommand(
                createPostRequest.memberId(),
                createPostRequest.matchingId(),
                createPostRequest.title(),
                createPostRequest.content()
        );
    }
}
