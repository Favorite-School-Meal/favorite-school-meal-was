package com.example.favoriteschoolmeal.domain.model;

public enum FriendRequestStatus {
    PENDING("대기중"),
    ACCEPTED("승인됨"),
    REJECTED("거절됨"),;

    private final String description;

    FriendRequestStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
