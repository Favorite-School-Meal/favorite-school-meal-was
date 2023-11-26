package com.example.favoriteschoolmeal.domain.notification.domain;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 알림 정보를 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends Base {

    /**
     * 알림의 고유 식별자입니다. 자동 생성되는 ID 값입니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", updatable = false)
    private Long id;

    /**
     * 알림을 받는 사용자를 나타냅니다. Member 엔티티와의 다대일(N:1) 관계를 맺고 있습니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    /**
     * 알림을 보낸 사용자의 ID를 나타냅니다. 알림을 생성한 사용자의 식별자입니다.
     */
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    /**
     * 알림과 관련된 게시물의 ID입니다.
     */
    @Column(name = "post_id", nullable = false)
    private Long postId;

    /**
     * 알림의 유형을 나타냅니다. NotificationType 열거형을 사용하여 알림의 유형을 정의합니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    /**
     * 알림의 읽음 여부를 나타냅니다. 기본값은 false(읽지 않음)입니다.
     */
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    /**
     * 알림 객체를 생성할 때 사용하는 빌더 메서드입니다.
     *
     * @param receiver         알림을 받는 사용자
     * @param senderId         알림을 보낸 사용자의 ID
     * @param postId           알림과 관련된 게시물 ID
     * @param notificationType 알림의 유형
     * @param isRead           알림의 읽음 여부
     */
    @Builder
    public Notification(Member receiver, Long senderId, Long postId,
            NotificationType notificationType,
            Boolean isRead) {
        this.receiver = receiver;
        this.senderId = senderId;
        this.postId = postId;
        this.notificationType = notificationType;
        this.isRead = isRead;
    }

    /**
     * 알림을 읽음 상태로 변경하는 메서드입니다.
     */
    public void readNotification() {
        this.isRead = true;
    }
}