package com.example.favoriteschoolmeal.domain.notification.controller;

import com.example.favoriteschoolmeal.domain.notification.controller.dto.NotificationListResponse;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.UnreadNotificationStatusResponse;
import com.example.favoriteschoolmeal.domain.notification.service.NotificationService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<NotificationListResponse> notificationList() {
        final NotificationListResponse response = notificationService.findAllNotification();
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/unread-status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UnreadNotificationStatusResponse> unreadNotificationsCheck() {
        final UnreadNotificationStatusResponse response = notificationService.hasUnreadNotifications();
        return ApiResponse.createSuccess(response);
    }
}
