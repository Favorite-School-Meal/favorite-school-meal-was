package com.example.favoriteschoolmeal.domain.notification.domain;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "content", nullable = false, length = 300)
    private String content;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Builder
    public Notification(Member member, String content, Boolean isRead) {
        this.member = member;
        this.content = content;
        this.isRead = isRead;
    }
}
