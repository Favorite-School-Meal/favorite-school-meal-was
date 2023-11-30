package com.example.favoriteschoolmeal.domain.friend.service;

import com.example.favoriteschoolmeal.domain.friend.controller.dto.FriendStatusResponse;
import com.example.favoriteschoolmeal.domain.friend.controller.dto.MemberFriendCountResponse;
import com.example.favoriteschoolmeal.domain.friend.domain.Friend;
import com.example.favoriteschoolmeal.domain.friend.exception.FriendException;
import com.example.favoriteschoolmeal.domain.friend.exception.FriendExceptionType;
import com.example.favoriteschoolmeal.domain.friend.repository.FriendRepository;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.dto.PaginatedMemberListResponse;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.FriendRequestStatus;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.service.NotificationService;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

        checkAlreadyExists(sender, receiver);

        checkSelfRequest(sender, receiver);

        Friend friend = Friend.builder()
                .sender(sender)
                .receiver(receiver)
                .friendRequestStatus(FriendRequestStatus.PENDING)
                .build();

        Friend savedFriend = friendRepository.save(friend);

        notificationService.createFriendNotification(
                sender.getId(), receiver.getId(), savedFriend.getId(), NotificationType.FRIEND_REQUESTED);

    }

    private static void checkSelfRequest(Member sender, Member receiver) {
        if(sender.getId().equals(receiver.getId())){
            throw new FriendException(FriendExceptionType.CANNOT_REQUEST_TO_MYSELF);
        }
    }


    public void cancelFriendRequest(Long memberId) {
        verifyUserOrAdmin();
        Member sender = getMemberOrThrow(getCurrentMemberId());
        Member receiver = getMemberOrThrow(memberId);
        Friend friend = getFriendRequestOrThrow(sender, receiver);

        //기존의 친구 신청 알림을 삭제
        notificationService.deleteByFriendIdAndNotificationType(friend.getId(), NotificationType.FRIEND_REQUESTED);

        friendRepository.delete(friend);

        //친구 신청 취소 알림 생성
        notificationService.createFriendNotification(sender.getId(), receiver.getId(),
                null, NotificationType.FRIEND_REQUEST_CANCELLED);

    }

    public void acceptFriendRequest(Long memberId) {
        verifyUserOrAdmin();
        Member friendRequestSender = getMemberOrThrow(memberId);
        Member friendRequestReceiver = getMemberOrThrow(getCurrentMemberId());
        Friend friend = getFriendRequestOrThrow(friendRequestSender, friendRequestReceiver);

        checkAlreadyFriend(friendRequestSender, friendRequestReceiver);

        friend.accept();

        //알림은 친구 신청을 받은 회원이 친구 신청을 보낸 회원에게 보냄
        notificationService.createFriendNotification(friendRequestReceiver.getId(), friendRequestSender.getId(),
                friend.getId(), NotificationType.FRIEND_REQUEST_ACCEPTED);
    }

    public void rejectFriendRequest(Long memberId) {
        verifyUserOrAdmin();
        Member friendRequestSender = getMemberOrThrow(memberId);
        Member friendRequestReceiver = getMemberOrThrow(getCurrentMemberId());
        Friend friend = getFriendRequestOrThrow(friendRequestSender, friendRequestReceiver);
        friend.reject();

        //알림은 친구 신청을 받은 회원이 친구 신청을 보낸 회원에게 보냄
        notificationService.createFriendNotification(friendRequestReceiver.getId(), friendRequestSender.getId(),
                friend.getId(), NotificationType.FRIEND_REQUEST_REJECTED);
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
        long friendCount = friendRepository.findAcceptedFriendByMemberId(member.getId(),
                Pageable.unpaged()).getTotalElements();
        return MemberFriendCountResponse.from(friendCount);

    }

    public void deleteFriend(Long memberId) {
        verifyUserOrAdmin();
        Member sender = getMemberOrThrow(getCurrentMemberId());
        Member receiver = getMemberOrThrow(memberId);
        Friend friend = getAcceptedFriendByMembersOrThrow(sender, receiver);

        System.out.println(friend.getId());
        //기존의 친구 신청 알림을 삭제
        notificationService.deleteByFriendIdAndNotificationType(friend.getId(), NotificationType.FRIEND_REQUESTED);

        friendRepository.delete(friend);

        //친구 삭제 알림 생성
        notificationService.createFriendNotification(sender.getId(), receiver.getId(), null, NotificationType.FRIEND_REMOVED);
    }


    @Transactional(readOnly = true)
    public FriendStatusResponse getFriendStatusByMemberId(Long memberId) {
        verifyUserOrAdmin();
        Member sender = getMemberOrThrow(getCurrentMemberId());
        Member receiver = getMemberOrThrow(memberId);
        Optional<Friend> friend = friendRepository.findByMembers(sender.getId(), receiver.getId());

        return FriendStatusResponse.from(friend);
    }

    public Friend getFriendOrThrow(Long friendId) {
        return friendRepository.findById(friendId)
                .orElseThrow(() -> new FriendException(FriendExceptionType.FRIEND_NOT_FOUND));
    }

    public String getFriendRequestStatus(Long friendId) {
        Friend friend = getFriendOrThrow(friendId);
        return friend.getFriendRequestStatus().toString();
    }

    private Friend getFriendRequestOrThrow(Member sender, Member receiver) {
        return friendRepository.findFriendRequestBySenderIdAndReceiverIdAndStatus(sender.getId(),
                        receiver.getId(), FriendRequestStatus.PENDING)
                .orElseThrow(
                        () -> new FriendException(FriendExceptionType.FRIEND_REQUEST_NOT_FOUND));
    }


    private void checkAlreadyFriend(Member sender, Member receiver) {
        if (friendRepository.findAcceptedFriendByMembers(sender.getId(), receiver.getId())
                .isPresent()) {
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

    private void checkAlreadyExists(Member sender, Member receiver) {
        if (friendRepository.findByMembers(sender.getId(), receiver.getId()).isPresent()) {
            throw new FriendException(FriendExceptionType.ALREADY_EXISTS);
        }
    }

}
