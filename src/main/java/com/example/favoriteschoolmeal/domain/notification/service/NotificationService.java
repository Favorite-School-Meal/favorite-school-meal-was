package com.example.favoriteschoolmeal.domain.notification.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.domain.Notification;
import com.example.favoriteschoolmeal.domain.notification.exception.NotificationException;
import com.example.favoriteschoolmeal.domain.notification.exception.NotificationExceptionType;
import com.example.favoriteschoolmeal.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;

    public NotificationService(final NotificationRepository notificationRepository,
            final MemberService memberService) {
        this.notificationRepository = notificationRepository;
        this.memberService = memberService;
    }

    public void createNotification(final Long postId, final Long memberId,
            final NotificationType notificationType) {
        Member member = getMemberOrThrow(memberId);
        Notification notification = Notification.builder()
                .member(member)
                .postId(postId)
                .notificationType(notificationType)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    private Member getMemberOrThrow(final Long memberId) {
        return memberService.findMemberOptionally(memberId)
                .orElseThrow(() -> new NotificationException(NotificationExceptionType.MEMBER_NOT_FOUND));
    }
}