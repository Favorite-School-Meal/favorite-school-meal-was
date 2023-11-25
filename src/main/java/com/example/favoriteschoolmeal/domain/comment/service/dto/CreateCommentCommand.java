package com.example.favoriteschoolmeal.domain.comment.service.dto;

import com.example.favoriteschoolmeal.domain.comment.controller.dto.CreateCommentRequest;

public record CreateCommentCommand(
        Long postId,
        String content) {

    public static CreateCommentCommand of(final CreateCommentRequest request, final Long postId) {
        return new CreateCommentCommand(postId, request.content());
    }
}
