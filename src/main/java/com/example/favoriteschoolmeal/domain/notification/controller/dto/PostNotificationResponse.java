package com.example.favoriteschoolmeal.domain.notification.controller.dto;

import com.example.favoriteschoolmeal.domain.notification.domain.PostNotification;

public record PostNotificationResponse(
        Long notificationId,
        Long postId,
        Long receiverId,
        Long senderId,
        Integer notificationType,
        String createdAt,
        Boolean isRead) implements NotificationResponse {

    public static PostNotificationResponse from(final PostNotification notification) {
        return new PostNotificationResponse(
                notification.getId(),
                notification.getPostId(),
                notification.getReceiver().getId(),
                notification.getSenderId(),
                notification.getNotificationType().getId(),
                notification.getFormattedCreatedAt(),
                notification.getIsRead()
        );
    }
}
