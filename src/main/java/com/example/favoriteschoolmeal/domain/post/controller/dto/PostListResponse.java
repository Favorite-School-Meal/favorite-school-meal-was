package com.example.favoriteschoolmeal.domain.post.controller.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PostListResponse(
        List<PostResponse> content,
        int currentPage,
        int totalPages,
        long totalElements) {

    public static PostListResponse from(Page<PostResponse> page) {
        return new PostListResponse(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements());
    }
}
