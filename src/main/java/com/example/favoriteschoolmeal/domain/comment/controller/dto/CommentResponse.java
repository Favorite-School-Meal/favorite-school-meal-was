package com.example.favoriteschoolmeal.domain.comment.controller.dto;

import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import java.util.List;
import java.util.stream.Collectors;

public record CommentResponse(
        Long memberId, // 사용자 아이디
        String username, // 사용자 이름
        Long commentId, // 댓글 아이디
        String content, // 댓글 내용
        String createdAt // 댓글 작성 시간
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
