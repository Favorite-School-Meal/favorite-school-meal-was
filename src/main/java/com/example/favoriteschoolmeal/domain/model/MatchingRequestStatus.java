package com.example.favoriteschoolmeal.domain.model;

public enum MatchingRequestStatus {
    PENDING("대기중"),
    ACCEPTED("승인됨"),
    REJECTED("거절됨"),
    CANCELED("취소됨");

    private final String description;

    MatchingRequestStatus(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
