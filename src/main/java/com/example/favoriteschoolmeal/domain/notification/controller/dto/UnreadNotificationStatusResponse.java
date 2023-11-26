package com.example.favoriteschoolmeal.domain.notification.controller.dto;

/**
 * 안 읽은 알림의 존재 유무를 나타내는 응답 객체입니다.
 */
public record UnreadNotificationStatusResponse(
        boolean hasUnread) {

    public static UnreadNotificationStatusResponse from(final boolean hasUnread) {
        return new UnreadNotificationStatusResponse(
                hasUnread
        );
    }
}
