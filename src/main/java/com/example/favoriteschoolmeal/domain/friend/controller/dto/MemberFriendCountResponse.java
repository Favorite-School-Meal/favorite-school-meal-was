package com.example.favoriteschoolmeal.domain.friend.controller.dto;

public record MemberFriendCountResponse(
        long friendCount
) {

    public static MemberFriendCountResponse from(final long friendCount) {
        return new MemberFriendCountResponse(
                friendCount
        );
    }
}
