package com.example.favoriteschoolmeal.domain.model;

public enum NotificationType {
    COMMENT_POSTED(1),
    MATCHING_REQUESTED(2),
    MATCHING_CANCELED(3),
    MATCHING_ACCEPTED(4),
    MATCHING_REJECTED(5),
    MATCHING_COMPLETED(6);

    private final int id;

    NotificationType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}