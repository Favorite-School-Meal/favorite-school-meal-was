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

@Service
@AllArgsConstructor
public class NotificationManageService {

    private final NotificationService notificationService;
    private final FriendService friendService;
    private final PostService postService;

    @Transactional(readOnly = true)
    public NotificationListResponse findAllNotification() {
        List<Notification> notifications = notificationService.findAllNotification();
        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(notification -> {
                    String status = null;
                    if (notification instanceof PostNotification postNotification
                            && notification.getNotificationType()
                            .equals(NotificationType.MATCHING_REQUESTED)) {
                        status = postService.getPostStatus(postNotification.getPostId(),
                                postNotification.getSenderId());
                    } else if (notification instanceof FriendNotification friendNotification
                            && notification.getNotificationType()
                            .equals(NotificationType.FRIEND_REQUESTED)) {
                        status = friendService.getFriendRequestStatus(
                                friendNotification.getFriendId());
                    }
                    return mapToNotificationResponse(notification, status);
                }).collect(Collectors.toList());

        return new NotificationListResponse(notificationResponses);
    }

    private NotificationResponse mapToNotificationResponse(Notification notification,
            String status) {
        if (notification instanceof PostNotification) {
            return PostNotificationResponse.from((PostNotification) notification, status);
        }
        if (notification instanceof FriendNotification) {
            return FriendNotificationResponse.from((FriendNotification) notification, status);
        }
        throw new NotificationException(NotificationExceptionType.UNSUPPORTED_NOTIFICATION_TYPE);
    }
}
