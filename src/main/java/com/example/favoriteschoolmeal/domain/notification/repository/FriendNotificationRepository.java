package com.example.favoriteschoolmeal.domain.notification.repository;

import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.domain.FriendNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendNotificationRepository extends JpaRepository<FriendNotification, Long> {

    /**
     * 주어진 친구 ID와 알림 유형에 해당하는 친구 요청 관련 알림을 삭제합니다.
     *
     * @param friendId          삭제할 알림과 관련된 친구의 ID
     * @param notificationType  삭제할 알림의 유형
     */
    void deleteByFriendIdAndNotificationType(Long friendId, NotificationType notificationType);
}