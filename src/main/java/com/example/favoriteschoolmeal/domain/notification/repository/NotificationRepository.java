package com.example.favoriteschoolmeal.domain.notification.repository;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.domain.FriendNotification;
import com.example.favoriteschoolmeal.domain.notification.domain.Notification;
import com.example.favoriteschoolmeal.domain.notification.domain.PostNotification;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 특정 멤버의 모든 알림을 조회합니다. 추가된 Sort 매개변수를 통해 결과를 정렬할 수 있습니다.
     *
     * @param receiver 알림을 조회할 멤버 객체
     * @param sort     정렬 기준
     * @return 주어진 멤버에 대한 정렬된 알림 리스트
     */
    List<Notification> findAllByReceiver(Member receiver, Sort sort);

    /**
     * 멤버에게 안 읽은 알림이 있는지 여부를 확인합니다.
     *
     * @param receiver 멤버 객체
     * @param isRead   읽음 상태
     * @return 안 읽은 알림 존재 여부
     */
    boolean existsByReceiverAndIsRead(Member receiver, boolean isRead);

    /**
     * 주어진 친구 ID와 알림 유형에 해당하는 친구 요청 관련 알림을 삭제합니다.
     *
     * @param friendId          삭제할 알림과 관련된 친구의 ID
     * @param notificationType  삭제할 알림의 유형
     */
    void deleteByFriendIdAndNotificationType(Long friendId, NotificationType notificationType);

    /**
     * 주어진 게시물 ID와 알림 유형에 해당하는 매칭 요청 관련 알림을 삭제합니다.
     *
     * @param postId            삭제할 알림과 관련된 게시물의 ID
     * @param notificationType  삭제할 알림의 유형
     */
    void deleteByPostIdAndNotificationType(Long postId, NotificationType notificationType);
}