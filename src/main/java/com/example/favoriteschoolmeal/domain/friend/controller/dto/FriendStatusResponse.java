package com.example.favoriteschoolmeal.domain.friend.controller.dto;

import com.example.favoriteschoolmeal.domain.friend.domain.Friend;
import com.example.favoriteschoolmeal.domain.model.FriendRequestStatus;

import java.util.Optional;

public record FriendStatusResponse(
        String friendRequestStatus
) {
    public static FriendStatusResponse from(final Optional<Friend> friend) {
        return new FriendStatusResponse(
                friend.map(Friend::getFriendRequestStatus)
                        .map(FriendRequestStatus::name)
                        .orElse(null));
    }
}
