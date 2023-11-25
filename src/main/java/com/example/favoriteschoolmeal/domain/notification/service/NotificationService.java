package com.example.favoriteschoolmeal.domain.notification.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.NotificationListResponse;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.NotificationResponse;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.UnreadNotificationStatusResponse;
import com.example.favoriteschoolmeal.domain.notification.domain.Notification;
import com.example.favoriteschoolmeal.domain.notification.exception.NotificationException;
import com.example.favoriteschoolmeal.domain.notification.exception.NotificationExceptionType;
import com.example.favoriteschoolmeal.domain.notification.repository.NotificationRepository;
import com.example.favoriteschoolmeal.domain.post.exception.PostException;
import com.example.favoriteschoolmeal.domain.post.exception.PostExceptionType;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.util.List;
import java.util.stream.Collectors;
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

    public NotificationListResponse findAllNotification() {
        Member member = getMemberOrThrow(getCurrentMemberId());
        List<NotificationResponse> notificationResponseList = notificationRepository
                .findAllByMember(member)
                .stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
        return NotificationListResponse.from(notificationResponseList);
    }

    public UnreadNotificationStatusResponse hasUnreadNotifications() {
        Member member = getMemberOrThrow(getCurrentMemberId());
        return UnreadNotificationStatusResponse.from(
                notificationRepository.existsByMemberAndIsRead(member, false));
    }

    private Member getMemberOrThrow(final Long memberId) {
        return memberService.findMemberOptionally(memberId)
                .orElseThrow(() -> new NotificationException(
                        NotificationExceptionType.MEMBER_NOT_FOUND));
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(
                () -> new PostException(PostExceptionType.MEMBER_NOT_FOUND));
    }
}