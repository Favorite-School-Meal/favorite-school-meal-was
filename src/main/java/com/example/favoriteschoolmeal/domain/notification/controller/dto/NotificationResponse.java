package com.example.favoriteschoolmeal.domain.notification.controller.dto;

import com.example.favoriteschoolmeal.domain.notification.domain.Notification;

public record NotificationResponse(
        Long notificationId,
        Long postId,
        Long memberId,
        Integer notificationType,
        String createdAt,
        Boolean isRead) {

    public static NotificationResponse from(final Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getPostId(),
                notification.getMember().getId(),
                notification.getNotificationType().getId(),
                notification.getFormattedCreatedAt(),
                notification.getIsRead()
        );
    }
}
