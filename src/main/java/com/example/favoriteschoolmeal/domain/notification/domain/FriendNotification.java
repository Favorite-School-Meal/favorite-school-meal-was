package com.example.favoriteschoolmeal.domain.notification.domain;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("FRIEND")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendNotification extends Notification {

    @Builder
    public FriendNotification(Member receiver, Long senderId, NotificationType notificationType,
            Boolean isRead) {
        super(receiver, senderId, notificationType);
    }
}
