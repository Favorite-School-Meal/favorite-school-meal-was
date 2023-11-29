package com.example.favoriteschoolmeal.domain.notification.repository;

import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.domain.PostNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostNotificationRepository extends JpaRepository<PostNotification, Long> {

    /**
     * 주어진 게시물 ID와 알림 유형에 해당하는 매칭 요청 관련 알림을 삭제합니다.
     * 이 메서드는 게시물이 삭제될 때 관련된 매칭 요청 알림을 청소하는 데 사용됩니다.
     *
     * @param postId           삭제할 알림과 관련된 게시물의 ID
     * @param notificationType 삭제할 알림의 유형
     */
    void deleteByPostIdAndNotificationType(Long postId, NotificationType notificationType);

    /**
     * 주어진 게시물 ID, 수신자 ID, 알림 유형에 해당하는 매칭 요청 관련 알림을 삭제합니다.
     * 이 메서드는 매칭 요청을 취소할 때 관련된 매칭 요청 알림을 청소하는 데 사용됩니다.
     *
     * @param postId           삭제할 알림과 관련된 게시물의 ID
     * @param senderId       삭제할 알림의 발신자 ID
     * @param notificationType 삭제할 알림의 유형
     */
    void deleteByPostIdAndSenderIdAndNotificationType(Long postId, Long senderId, NotificationType notificationType);
}