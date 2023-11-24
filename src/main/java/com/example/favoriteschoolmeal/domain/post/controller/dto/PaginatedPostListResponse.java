package com.example.favoriteschoolmeal.domain.post.controller.dto;

import java.util.List;

public record PaginatedPostListResponse(
        List<PostSummaryResponse> content,
        int currentPage,
        int totalPages,
        long totalElements) {

    public static PaginatedPostListResponse from(final List<PostSummaryResponse> content,
            final int currentPage,
            final int totalPages, final long totalElements) {
        return new PaginatedPostListResponse(
                content,
                currentPage,
                totalPages,
                totalElements
        );
    }
}
