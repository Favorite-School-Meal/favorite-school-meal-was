package com.example.favoriteschoolmeal.domain.notification.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.controller.dto.UnreadNotificationStatusResponse;
import com.example.favoriteschoolmeal.domain.notification.domain.FriendNotification;
import com.example.favoriteschoolmeal.domain.notification.domain.Notification;
import com.example.favoriteschoolmeal.domain.notification.domain.PostNotification;
import com.example.favoriteschoolmeal.domain.notification.exception.NotificationException;
import com.example.favoriteschoolmeal.domain.notification.exception.NotificationExceptionType;
import com.example.favoriteschoolmeal.domain.notification.repository.FriendNotificationRepository;
import com.example.favoriteschoolmeal.domain.notification.repository.NotificationRepository;
import com.example.favoriteschoolmeal.domain.notification.repository.PostNotificationRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.util.List;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 알림 관련 서비스를 제공하는 클래스입니다. 이 서비스는 알림 생성, 조회 및 읽음 처리 등의 기능을 담당합니다.
 */
@Service
@Transactional
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PostNotificationRepository postNotificationRepository;
    private final FriendNotificationRepository friendNotificationRepository;
    private final MemberService memberService;

    /**
     * 게시글 관련 알림을 생성합니다.
     *
     * @param senderId         알림 발신자 ID
     * @param receiverId       알림 수신자 ID
     * @param postId           관련 게시글 ID
     * @param notificationType 알림 유형
     */
    public void createPostNotification(final Long senderId, final Long receiverId,
            final Long postId, final NotificationType notificationType) {
        validateNotificationType(notificationType, NotificationType::isPostRelated);
        Member receiver = getMemberOrThrow(receiverId);
        PostNotification notification = PostNotification.builder()
                .senderId(senderId)
                .receiver(receiver)
                .postId(postId)
                .notificationType(notificationType)
                .build();
        notificationRepository.save(notification);
    }

    /**
     * 친구 관련 알림을 생성합니다.
     *
     * @param senderId         알림 발신자 ID
     * @param receiverId       알림 수신자 ID
     * @param friendId         관련 친구 ID
     * @param notificationType 알림 유형
     */
    public void createFriendNotification(final Long senderId, final Long receiverId,
            final Long friendId, final NotificationType notificationType) {
        validateNotificationType(notificationType, NotificationType::isFriendRelated);
        Member receiver = getMemberOrThrow(receiverId);
        FriendNotification notification = FriendNotification.builder()
                .senderId(senderId)
                .receiver(receiver)
                .friendId(friendId)
                .notificationType(notificationType)
                .build();
        notificationRepository.save(notification);
    }

    /**
     * 사용자의 모든 알림을 조회합니다. 생성 시간 내림차순으로 정렬하여 반환합니다.
     *
     * @return NotificationListResponse 사용자의 모든 알림 목록
     */
    @Transactional(readOnly = true)
    public List<Notification> findAllNotification() {
        verifyUserOrAdmin();
        Member receiver = getMemberOrThrow(getCurrentMemberId());
        return notificationRepository
                .findAllByReceiver(receiver, Sort.by("createdAt").descending());
    }

    /**
     * 사용자에게 안 읽은 알림이 있는지 확인합니다.
     *
     * @return UnreadNotificationStatusResponse 안 읽은 알림 여부
     */
    @Transactional(readOnly = true)
    public UnreadNotificationStatusResponse hasUnreadNotifications() {
        verifyUserOrAdmin();
        Member receiver = getMemberOrThrow(getCurrentMemberId());
        return UnreadNotificationStatusResponse.from(
                notificationRepository.existsByReceiverAndIsRead(receiver, false));
    }

    /**
     * 특정 알림을 읽음으로 처리합니다.
     *
     * @param notificationId 읽을 알림의 ID
     */
    public void readNotification(final Long notificationId) {
        verifyUserOrAdmin();
        Notification notification = getNotificationOrThrow(notificationId);
        verifyNotificationReceiver(notification, getCurrentMemberId());
        notification.readNotification();
        notificationRepository.save(notification);
    }

    /**
     * 주어진 친구 ID에 해당하는 친구 요청 관련 알림을 삭제합니다. 이 메서드는 사용자가 친구 요청을 처리한 후 관련 알림을 청소하는 데 사용됩니다.
     *
     * @param friendId 삭제할 알림과 관련된 친구의 ID
     */
    public void deleteByFriendIdAndNotificationType(Long friendId,
            NotificationType notificationType) {
        friendNotificationRepository.deleteByFriendIdAndNotificationType(
                friendId, notificationType
        );
    }

    /**
     * 주어진 게시물 ID에 해당하는 매칭 요청 관련 알림을 삭제합니다. 이 메서드는 게시물이 삭제될 때 관련된 매칭 요청 알림을 청소하는 데 사용됩니다.
     *
     * @param postId 삭제할 알림과 관련된 게시물의 ID
     */
    public void deleteByPostIdAndNotificationType(Long postId, NotificationType notificationType) {
        postNotificationRepository.deleteByPostIdAndNotificationType(
                postId, notificationType
        );
    }

    /**
     * 주어진 게시물 ID, 수신자 ID, 알림 유형에 해당하는 매칭 요청 관련 알림을 삭제합니다. 이 메서드는 매칭 요청을 취소할 때 관련된 매칭 요청 알림을 청소하는 데
     * 사용됩니다.
     *
     * @param postId           삭제할 알림과 관련된 게시물의 ID
     * @param senderId         삭제할 알림의 발신자 ID
     * @param notificationType 삭제할 알림의 유형
     */
    public void deleteByPostIdAndSenderIdAndNotificationType(Long postId, Long senderId,
            NotificationType notificationType) {
        postNotificationRepository.deleteByPostIdAndSenderIdAndNotificationType(postId, senderId,
                notificationType);
    }

    private void validateNotificationType(NotificationType notificationType,
            Predicate<NotificationType> validationPredicate) {
        if (!validationPredicate.test(notificationType)) {
            throw new NotificationException(NotificationExceptionType.INVALID_NOTIFICATION_TYPE);
        }
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