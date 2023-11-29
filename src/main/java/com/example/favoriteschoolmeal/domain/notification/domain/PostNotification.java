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
@DiscriminatorValue("POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostNotification extends Notification {

    /**
     * 알림과 관련된 게시물의 ID입니다.
     */
    @Column(name = "post_id")
    private Long postId;

    @Builder
    public PostNotification(Member receiver, Long senderId, NotificationType notificationType,
            Boolean isRead, Long postId) {
        super(receiver, senderId, notificationType);
        this.postId = postId;
    }
}
