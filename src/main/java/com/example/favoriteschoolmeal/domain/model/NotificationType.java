package com.example.favoriteschoolmeal.domain.model;

/**
 * 알림의 유형을 정의하는 열거형 클래스입니다.
 */
public enum NotificationType {

    /**
     * 댓글이 게시된 경우의 알림 유형입니다.
     */
    COMMENT_POSTED(1),

    /**
     * 매칭 요청이 발생했을 경우의 알림 유형입니다.
     */
    MATCHING_REQUESTED(2),

    /**
     * 매칭 요청이 취소되었을 경우의 알림 유형입니다.
     */
    MATCHING_REQUEST_CANCELED(3),

    /**
     * 매칭 요청이 수락되었을 경우의 알림 유형입니다.
     */
    MATCHING_REQUEST_ACCEPTED(4),

    /**
     * 매칭 요청이 거부되었을 경우의 알림 유형입니다.
     */
    MATCHING_REQUEST_REJECTED(5),

    /**
     * 매칭이 완료되었을 경우의 알림 유형입니다.
     */
    MATCHING_COMPLETED(6),

    /**
     * 친구 요청이 발생했을 경우의 알림 유형입니다.
     */
    FRIEND_REQUESTED(7),

    /**
     * 친구 요청이 수락되었을 경우의 알림 유형입니다.
     */
    FRIEND_REQUEST_ACCEPTED(8),

    /**
     * 친구 요청이 거부되었을 경우의 알림 유형입니다.
     */
    FRIEND_REQUEST_REJECTED(9),

    /**
     * 친구가 제거되었을 경우의 알림 유형입니다.
     */
    FRIEND_REMOVED(10);

    private final int id;

    NotificationType(final int id) {
        this.id = id;
    }

    /**
     * 알림 유형의 식별자를 반환합니다.
     *
     * @return 알림 유형의 식별자
     */
    public int getId() {
        return id;
    }

    /**
     * 알림 유형이 게시물과 관련된 알림인지 확인합니다.
     *
     * @return 게시물과 관련된 알림인지 여부
     */
    public boolean isPostRelated() {
        return this == COMMENT_POSTED || this == MATCHING_REQUESTED
                || this == MATCHING_REQUEST_CANCELED
                || this == MATCHING_REQUEST_ACCEPTED || this == MATCHING_REQUEST_REJECTED
                || this == MATCHING_COMPLETED;
    }

    /**
     * 알림 유형이 친구와 관련된 알림인지 확인합니다.
     *
     * @return 친구와 관련된 알림인지 여부
     */
    public boolean isFriendRelated() {
        return this == FRIEND_REQUESTED || this == FRIEND_REQUEST_ACCEPTED
                || this == FRIEND_REQUEST_REJECTED || this == FRIEND_REMOVED;
    }
}