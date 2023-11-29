package com.example.favoriteschoolmeal.domain.notification.domain;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("FRIEND")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendNotification extends Notification {

    /**
     * 알림과 관련된 친구의 ID입니다.
     */
    @Column(name = "friend_id")
    private Long friendId;

    @Builder
    public FriendNotification(Member receiver, Long senderId, NotificationType notificationType,
            Boolean isRead, Long friendId) {
        super(receiver, senderId, notificationType);
        this.friendId = friendId;
    }
}
