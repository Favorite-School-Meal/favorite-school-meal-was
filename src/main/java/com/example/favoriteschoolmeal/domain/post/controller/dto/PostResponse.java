package com.example.favoriteschoolmeal.domain.post.controller.dto;

import com.example.favoriteschoolmeal.domain.post.domain.Post;

public record PostResponse(Long id, Long memberId, Long matchingId, String title, String content) {

    public static PostResponse from(final Post post) {
        return new PostResponse(
                post.getId(),
                post.getMember().getId(),
                post.getMatching().getId(),
                post.getTitle(),
                post.getContent()
        );
    }
}
