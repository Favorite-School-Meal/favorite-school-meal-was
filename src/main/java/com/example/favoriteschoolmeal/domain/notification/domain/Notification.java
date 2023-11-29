package com.example.favoriteschoolmeal.domain.notification.domain;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 알림 정보를 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "notification")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Notification extends Base {

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
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    /**
     * 알림을 보낸 사용자의 ID를 나타냅니다. 알림을 생성한 사용자의 식별자입니다.
     */
    @Column(name = "sender_id", nullable = false)
    private Long senderId;

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
    private Boolean isRead = false;

    protected Notification(Member receiver, Long senderId, NotificationType notificationType) {
        this.receiver = receiver;
        this.senderId = senderId;
        this.notificationType = notificationType;
    }

    /**
     * 알림을 읽음 상태로 변경하는 메서드입니다.
     */
    public void readNotification() {
        this.isRead = true;
    }
}