package com.example.favoriteschoolmeal.domain.post.controller.dto;

// TODO: 의존성 추가 후 @NotNull, @NotBlank 어노테이션 추가
public record CreatePostRequest(
        Long memberId, Long matchingId, String title, String content) {

}
