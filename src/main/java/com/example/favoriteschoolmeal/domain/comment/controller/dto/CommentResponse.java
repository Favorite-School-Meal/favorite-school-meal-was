package com.example.favoriteschoolmeal.domain.comment.controller.dto;

import com.example.favoriteschoolmeal.domain.comment.domain.Comment;

public record CommentResponse(
        Long writerId, // 사용자 아이디
        String writer, // 사용자 이름
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
}
