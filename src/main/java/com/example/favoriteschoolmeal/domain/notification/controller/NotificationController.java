package com.example.favoriteschoolmeal.domain.notification.controller;

import com.example.favoriteschoolmeal.domain.notification.controller.dto.NotificationListResponse;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.NotificationResponse;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.UnreadNotificationStatusResponse;
import com.example.favoriteschoolmeal.domain.notification.service.NotificationService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 알림 관련 요청을 처리하는 컨트롤러입니다.
 * 이 클래스는 알림 리스트 조회, 안 읽은 알림 상태 확인, 특정 알림 읽음 처리 등의 API를 제공합니다.
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 사용자의 모든 알림 목록을 조회하는 API입니다.
     * HTTP 상태 코드 200(OK)와 함께 조회된 알림 목록을 반환합니다.
     *
     * @return ApiResponse<NotificationListResponse> 형태로 알림 목록을 반환
     */
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<NotificationListResponse> notificationList() {
        final NotificationListResponse response = notificationService.findAllNotification();
        return ApiResponse.createSuccess(response);
    }

    /**
     * 사용자의 안 읽은 알림이 있는지 확인하는 API입니다.
     * HTTP 상태 코드 200(OK)와 함께 안 읽은 알림 여부를 반환합니다.
     *
     * @return ApiResponse<UnreadNotificationStatusResponse> 형태로 안 읽은 알림 여부를 반환
     */
    @GetMapping("/unread-status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UnreadNotificationStatusResponse> unreadNotificationsCheck() {
        final UnreadNotificationStatusResponse response = notificationService.hasUnreadNotifications();
        return ApiResponse.createSuccess(response);
    }

    /**
     * 특정 알림을 읽음으로 처리하는 API입니다.
     * 해당 알림 ID를 받아 알림을 읽음 상태로 변경하고, 변경된 알림 정보를 반환합니다.
     *
     * @param notificationId 읽을 알림의 ID
     * @return ApiResponse<NotificationResponse> 형태로 읽음 처리된 알림 정보를 반환
     */
    @PatchMapping("/{notificationId}/read")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<NotificationResponse> notificationRead(
            @PathVariable final Long notificationId) {
        final NotificationResponse response = notificationService.readNotification(notificationId);
        return ApiResponse.createSuccess(response);
    }
}
