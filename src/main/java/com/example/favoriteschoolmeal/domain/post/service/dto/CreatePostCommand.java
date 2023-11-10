package com.example.favoriteschoolmeal.domain.post.service.dto;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import com.example.favoriteschoolmeal.domain.post.domain.Post;

// TODO: 의존성 추가 후 @NotNull, @NotBlank 어노테이션 추가
public record CreatePostCommand(
        Long memberId, Long matchingId, String title, String content) {

    public static CreatePostCommand from(final CreatePostRequest createPostRequest) {
        return new CreatePostCommand(
                createPostRequest.memberId(),
                createPostRequest.matchingId(),
                createPostRequest.title(),
                createPostRequest.content()
        );
    }
    public Post toEntity(final Member member, final Matching matching) {
        return Post.builder()
                .member(member)
                .matching(matching)
                .title(title)
                .content(content)
                .build();
    }
}
