package com.example.favoriteschoolmeal.domain.comment.controller.dto;

import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import java.util.List;
import java.util.stream.Collectors;

public record CommentResponse(
        Long memberId,
        String username,
        Long commentId,
        String content,
        String createdAt
) {

    public static CommentResponse from(final Comment comment) {
        return new CommentResponse(
                comment.getMember().getId(),
                comment.getMember().getUsername(),
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    public static List<CommentResponse> listFrom(final List<Comment> comments) {
        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }
}
