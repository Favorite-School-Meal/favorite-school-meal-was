package com.example.favoriteschoolmeal.domain.friend.controller.dto;

import com.example.favoriteschoolmeal.domain.model.FriendRequestStatus;

public record FriendStatusResponse(
        String friendRequestStatus
) {
    public static FriendStatusResponse from(final FriendRequestStatus friendRequestStatus) {
        return new FriendStatusResponse(friendRequestStatus.name());
    }
}
