package com.example.favoriteschoolmeal.domain.notification.service;

import com.example.favoriteschoolmeal.domain.friend.service.FriendService;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.FriendNotificationResponse;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.NotificationListResponse;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.NotificationResponse;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.PostNotificationResponse;
import com.example.favoriteschoolmeal.domain.notification.domain.FriendNotification;
import com.example.favoriteschoolmeal.domain.notification.domain.Notification;
import com.example.favoriteschoolmeal.domain.notification.domain.PostNotification;
import com.example.favoriteschoolmeal.domain.notification.exception.NotificationException;
import com.example.favoriteschoolmeal.domain.notification.exception.NotificationExceptionType;
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 알림 관련 서비스의 퍼사드(Facade) 클래스입니다.
 * 이 클래스는 알림과 관련된 다양한 서비스를 조합하여 복잡한 비즈니스 로직을 단순화합니다.
 */
@Service
@Transactional
@AllArgsConstructor
public class NotificationFacadeService {

    private final NotificationService notificationService;
    private final FriendService friendService;
    private final PostService postService;

    /**
     * 모든 알림을 조회하고, 각 알림에 대한 상태 정보를 포함한 응답 객체를 반환합니다.
     *
     * @return 알림 목록과 상태 정보가 포함된 NotificationListResponse 객체
     */
    @Transactional(readOnly = true)
    public NotificationListResponse findAllNotification() {
        List<Notification> notifications = notificationService.findAllNotification();
        return mapNotificationsToResponse(notifications);
    }

    /**
     * 알림 목록을 NotificationResponse 객체 목록으로 변환합니다.
     *
     * @param notifications 변환할 알림 목록
     * @return 변환된 NotificationResponse 객체 목록
     */
    private NotificationListResponse mapNotificationsToResponse(List<Notification> notifications) {
        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(this::createNotificationResponse)
                .collect(Collectors.toList());

        return new NotificationListResponse(notificationResponses);
    }

    /**
     * 단일 알림을 NotificationResponse 객체로 변환합니다.
     * 알림 유형에 따라 적절한 상태 정보를 조회합니다.
     *
     * @param notification 변환할 알림 객체
     * @return 변환된 NotificationResponse 객체
     */
    private NotificationResponse createNotificationResponse(Notification notification) {
        String status = determineStatusBasedOnType(notification);
        return mapToNotificationResponse(notification, status);
    }

    /**
     * 알림 유형에 따라 상태 정보를 결정합니다.
     *
     * @param notification 상태를 결정할 알림 객체
     * @return 결정된 상태 문자열
     */
    private String determineStatusBasedOnType(Notification notification) {
        if (notification instanceof PostNotification postNotification
                && notification.getNotificationType().equals(NotificationType.MATCHING_REQUESTED)) {
            return postService.getPostStatus(postNotification.getPostId(),
                    postNotification.getSenderId());
        } else if (notification instanceof FriendNotification friendNotification
                && notification.getNotificationType().equals(NotificationType.FRIEND_REQUESTED)) {
            return friendService.getFriendRequestStatus(friendNotification.getFriendId());
        }
        return null;
    }

    /**
     * 알림 객체와 상태 문자열을 기반으로 NotificationResponse 객체를 생성합니다.
     *
     * @param notification 변환할 알림 객체
     * @param status 알림의 상태 문자열
     * @return 생성된 NotificationResponse 객체
     */
    private NotificationResponse mapToNotificationResponse(Notification notification,
            String status) {
        if (notification instanceof PostNotification) {
            return PostNotificationResponse.from((PostNotification) notification, status);
        } else if (notification instanceof FriendNotification) {
            return FriendNotificationResponse.from((FriendNotification) notification, status);
        }
        throw new NotificationException(NotificationExceptionType.UNSUPPORTED_NOTIFICATION_TYPE);
    }
}
