package com.example.favoriteschoolmeal.domain.notification.controller.dto;

import java.util.List;

public record NotificationListResponse(
        List<NotificationResponse> notificationList) {

    public static NotificationListResponse from(final List<NotificationResponse> notificationList) {
        return new NotificationListResponse(
                notificationList
        );
    }
}
