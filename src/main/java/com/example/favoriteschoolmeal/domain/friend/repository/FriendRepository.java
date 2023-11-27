package com.example.favoriteschoolmeal.domain.friend.repository;

import com.example.favoriteschoolmeal.domain.friend.domain.Friend;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT f FROM Friend f " +
            "WHERE (f.sender.id = :senderId AND f.receiver.id = :receiverId " +
            "AND f.friendRequestStatus = :status) " )
    Optional<Friend> findFriendRequestBySenderIdAndReceiverIdAndStatus(@Param("senderId") Long senderId,
                                              @Param("receiverId") Long receiverId,
                                              @Param("status") FriendRequestStatus status);


    @Query("SELECT f.sender FROM Friend f WHERE f.receiver.id = :memberId AND f.friendRequestStatus = 'ACCEPTED'" +
            "UNION " +
            "SELECT f.receiver FROM Friend f WHERE f.sender.id = :memberId AND f.friendRequestStatus = 'ACCEPTED'")
    Page<Member> findAcceptedFriendByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT f FROM Friend f " +
            "WHERE f.receiver.id = :receiverId AND f.sender.id = :senderId AND f.friendRequestStatus = 'ACCEPTED'" +
            "UNION " +
            "SELECT f FROM Friend f " +
            "WHERE f.sender.id = :receiverId AND f.receiver.id = :senderId AND f.friendRequestStatus = 'ACCEPTED'")
    Optional<Friend> findAcceptedFriendByMembers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
