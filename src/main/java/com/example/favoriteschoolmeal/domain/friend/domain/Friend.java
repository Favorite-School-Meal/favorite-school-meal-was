package com.example.favoriteschoolmeal.domain.friend.domain;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.FriendRequestStatus;
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

@Entity
@Table(name = "friend")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "friend_request_status", nullable = false)
    private FriendRequestStatus friendRequestStatus;

    @Builder
    public Friend(Member sender, Member receiver, FriendRequestStatus friendRequestStatus) {
        this.sender = sender;
        this.receiver = receiver;
        this.friendRequestStatus = friendRequestStatus;
    }

    public void accept() {
        this.friendRequestStatus = FriendRequestStatus.ACCEPTED;
    }

    public void reject() {
        this.friendRequestStatus = FriendRequestStatus.REJECTED;
    }


}
