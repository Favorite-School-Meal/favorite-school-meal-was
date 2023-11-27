package com.example.favoriteschoolmeal.domain.matching.controller.dto;

public record MemberMatchingCountResponse(
        long matchingCount) {

    public static MemberMatchingCountResponse from(final long matchingCount) {
        return new MemberMatchingCountResponse(
                matchingCount
        );
    }
}
