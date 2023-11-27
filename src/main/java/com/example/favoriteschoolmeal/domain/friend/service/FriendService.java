package com.example.favoriteschoolmeal.domain.friend.service;

import com.example.favoriteschoolmeal.domain.friend.controller.dto.MemberFriendCountResponse;
import com.example.favoriteschoolmeal.domain.friend.domain.Friend;
import com.example.favoriteschoolmeal.domain.friend.exception.FriendException;
import com.example.favoriteschoolmeal.domain.friend.exception.FriendExceptionType;
import com.example.favoriteschoolmeal.domain.friend.repository.FriendRepository;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.dto.PaginatedMemberListResponse;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.FriendRequestStatus;
import com.example.favoriteschoolmeal.domain.notification.service.NotificationService;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final MemberService memberService;
    private final NotificationService notificationService;

    public void requestFriend(Long memberId) {
        verifyUserOrAdmin();
        Member sender = getMemberOrThrow(getCurrentMemberId());
        Member receiver = getMemberOrThrow(memberId);

        checkAlreadyFriend(sender, receiver);
        checkAlreadyRequested(sender, receiver);

        Friend friend = Friend.builder()
                .sender(sender)
                .receiver(receiver)
                .friendRequestStatus(FriendRequestStatus.PENDING)
                .build();

        friendRepository.save(friend);

        //TODO: NOTIFICATION_TYPE.FRIEND_REQUEST
//        notificationService.createNotification(sender.getId(), receiver.getId(), null, NotificationType.COMMENT_POSTED);

    }

    public void cancelFriendRequest(Long memberId) {
        verifyUserOrAdmin();
        Member sender = getMemberOrThrow(getCurrentMemberId());
        Member receiver = getMemberOrThrow(memberId);
        Friend friend = getFriendRequestOrThrow(sender, receiver);
        friendRepository.delete(friend);
    }

    public void acceptFriendRequest(Long memberId) {
        verifyUserOrAdmin();
        Member sender = getMemberOrThrow(memberId);
        Member receiver = getMemberOrThrow(getCurrentMemberId());
        Friend friend = getFriendRequestOrThrow(sender, receiver);

        checkAlreadyFriend(sender, receiver);

        friend.accept();

        //TODO: NOTIFICATION_TYPE.FRIEND_REQUEST_ACCEPTED
        //알림은 sender와 receiver가 반대로 되어야 함
//        notificationService.createNotification(receiver.getId(), sender.getId(), null, NotificationType.COMMENT_POSTED);

    }

    public void rejectFriendRequest(Long memberId) {
        verifyUserOrAdmin();
        Member sender = getMemberOrThrow(memberId);
        Member receiver = getMemberOrThrow(getCurrentMemberId());
        Friend friend = getFriendRequestOrThrow(sender, receiver);
        friend.reject();
        //TODO: NOTIFICATION_TYPE.FRIEND_REQUEST_REJECTED
        //알림은 sender와 receiver가 반대로 되어야 함
//        notificationService.createNotification(receiver.getId(), sender.getId(), null, NotificationType.COMMENT_POSTED);
    }

    @Transactional(readOnly = true)
    public PaginatedMemberListResponse findAllFriends(Pageable pageable) {
        verifyUserOrAdmin();
        Long memberId = getCurrentMemberId();
        Page<Member> friends = friendRepository.findAcceptedFriendByMemberId(memberId, pageable);
        return memberService.getPaginatedMemberListResponse(friends);

    }

    @Transactional(readOnly = true)
    public MemberFriendCountResponse countFriend(Long memberId) {
        Member member = getMemberOrThrow(memberId);
        long friendCount = friendRepository.findAcceptedFriendByMemberId(member.getId(), Pageable.unpaged()).getTotalElements();
        return MemberFriendCountResponse.from(friendCount);

    }

    public void deleteFriend(Long memberId) {
        verifyUserOrAdmin();
        Member sender = getMemberOrThrow(getCurrentMemberId());
        Member receiver = getMemberOrThrow(memberId);
        Friend friend = getAcceptedFriendByMembersOrThrow(sender, receiver);
        friendRepository.delete(friend);
        //TODO: NOTIFICATION_TYPE.FRIEND_DELETED
    }
    private Friend getFriendRequestOrThrow(Member sender, Member receiver) {
        return friendRepository.findFriendRequestBySenderIdAndReceiverIdAndStatus(sender.getId(), receiver.getId(), FriendRequestStatus.PENDING)
                .orElseThrow(() -> new FriendException(FriendExceptionType.FRIEND_REQUEST_NOT_FOUND));
    }

    private void checkAlreadyRequested(Member sender, Member receiver) {
        if (friendRepository.findFriendRequestBySenderIdAndReceiverIdAndStatus(sender.getId(), receiver.getId(), FriendRequestStatus.PENDING).isPresent()) {
            throw new FriendException(FriendExceptionType.ALREADY_REQUESTED);
        }
    }

    private void checkAlreadyFriend(Member sender, Member receiver) {
        if (friendRepository.findAcceptedFriendByMembers(sender.getId(), receiver.getId()).isPresent()) {
            throw new FriendException(FriendExceptionType.ALREADY_FRIEND);
        }
    }

    private Member getMemberOrThrow(Long memberId) {
        return memberService.findMemberOptionally(memberId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.MEMBER_NOT_FOUND));
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(
                () -> new FriendException(FriendExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyUserOrAdmin() {
        SecurityUtils.checkUserOrAdminOrThrow(
                () -> new FriendException(FriendExceptionType.UNAUTHORIZED_ACCESS));
    }

    private Friend getAcceptedFriendByMembersOrThrow(Member sender, Member receiver) {
        return friendRepository.findAcceptedFriendByMembers(sender.getId(), receiver.getId())
                .orElseThrow(() -> new FriendException(FriendExceptionType.FRIEND_NOT_FOUND));
    }


}
