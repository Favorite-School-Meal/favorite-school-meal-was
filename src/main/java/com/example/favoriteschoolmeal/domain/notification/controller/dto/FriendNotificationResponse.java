package com.example.favoriteschoolmeal.domain.notification.controller.dto;

import com.example.favoriteschoolmeal.domain.notification.domain.FriendNotification;

public record FriendNotificationResponse(
        Long notificationId,
        Long receiverId,
        Long senderId,
        Integer notificationType,
        String createdAt,
        Boolean isRead) implements NotificationResponse {

    public static FriendNotificationResponse from(final FriendNotification notification) {
        return new FriendNotificationResponse(
                notification.getId(),
                notification.getReceiver().getId(),
                notification.getSenderId(),
                notification.getNotificationType().getId(),
                notification.getFormattedCreatedAt(),
                notification.getIsRead()
        );
    }
}
