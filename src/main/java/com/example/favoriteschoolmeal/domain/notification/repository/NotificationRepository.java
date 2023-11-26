package com.example.favoriteschoolmeal.domain.notification.repository;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.notification.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 특정 멤버의 모든 알림을 조회합니다.
     *
     * @param receiver 알림을 조회할 멤버 객체
     * @return 주어진 멤버에 대한 알림 리스트
     */
    List<Notification> findAllByReceiver(Member receiver);

    /**
     * 멤버에게 안 읽은 알림이 있는지 여부를 확인합니다.
     *
     * @param receiver 멤버 객체
     * @param isRead   읽음 상태
     * @return 안 읽은 알림 존재 여부
     */
    boolean existsByReceiverAndIsRead(Member receiver, boolean isRead);
}