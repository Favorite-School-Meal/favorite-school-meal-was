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

    public void createNotification(final Long senderId, final Long receiverId, final Long postId,
            final NotificationType notificationType) {
        Member member = getMemberOrThrow(receiverId);
        Notification notification = Notification.builder()
                .senderId(senderId)
                .receiver(member)
                .postId(postId)
                .notificationType(notificationType)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public NotificationListResponse findAllNotification() {
        verifyUserOrAdmin();
        Member receiver = getMemberOrThrow(getCurrentMemberId());
        List<NotificationResponse> notificationResponseList = notificationRepository
                .findAllByReceiver(receiver)
                .stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
        return NotificationListResponse.from(notificationResponseList);
    }

    @Transactional(readOnly = true)
    public UnreadNotificationStatusResponse hasUnreadNotifications() {
        verifyUserOrAdmin();
        Member receiver = getMemberOrThrow(getCurrentMemberId());
        return UnreadNotificationStatusResponse.from(
                notificationRepository.existsByReceiverAndIsRead(receiver, false));
    }

    public NotificationResponse readNotification(final Long notificationId) {
        verifyUserOrAdmin();
        Notification notification = getNotificationOrThrow(notificationId);
        verifyNotificationReceiver(notification, getCurrentMemberId());
        notification.readNotification();
        Notification savedNotification = notificationRepository.save(notification);
        return NotificationResponse.from(savedNotification);
    }

    private Notification getNotificationOrThrow(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(
                        NotificationExceptionType.NOTIFICATION_NOT_FOUND));
    }

    private Member getMemberOrThrow(final Long memberId) {
        return memberService.findMemberOptionally(memberId)
                .orElseThrow(() -> new NotificationException(
                        NotificationExceptionType.MEMBER_NOT_FOUND));
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(
                () -> new NotificationException(NotificationExceptionType.MEMBER_NOT_FOUND));
    }

    private void verifyUserOrAdmin() {
        SecurityUtils.checkUserOrAdminOrThrow(
                () -> new NotificationException(NotificationExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyNotificationReceiver(Notification notification, Long currentMemberId) {
        if (!notification.getReceiver().getId().equals(currentMemberId)) {
            throw new NotificationException(NotificationExceptionType.UNAUTHORIZED_ACCESS);
        }
    }
}