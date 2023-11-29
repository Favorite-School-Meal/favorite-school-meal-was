package com.example.favoriteschoolmeal.domain.member.dto;

import java.util.List;

public record PaginatedMemberListResponse(
        List<MemberSummaryResponse> content,
        int currentPage,
        int totalPages,
        long totalElements) {

    public static PaginatedMemberListResponse from(final List<MemberSummaryResponse> content,
            final int currentPage, final int totalPages, final long totalElements) {
        return new PaginatedMemberListResponse(
                content,
                currentPage,
                totalPages,
                totalElements
        );
    }
}
