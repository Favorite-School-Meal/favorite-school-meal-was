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
    /**
     * senderId와 receiverId, status로 친구 엔티티를 찾는 메서드입니다.
     *
     * @param senderId 친구 요청 신청자 id
     * @param receiverId 친구 요청 수신자 id
     * @param status 친구 요청 상태
     *
     * @return 친구 엔티티
     */
    @Query("SELECT f FROM Friend f " +
            "WHERE (f.sender.id = :senderId AND f.receiver.id = :receiverId " +
            "AND f.friendRequestStatus = :status) ")
    Optional<Friend> findFriendRequestBySenderIdAndReceiverIdAndStatus(@Param("senderId") Long senderId,
                                                                       @Param("receiverId") Long receiverId,
                                                                       @Param("status") FriendRequestStatus status);


    /**
     * memberId에 해당하는 회원과 승인된 친구 관계인 회원들을 찾는 메서드입니다.
     * 승인된 friend 중에서 memberId에 해당하는 회원이 sender인 경우와 receiver인 경우를 모두 찾고, 그 상대방 회원을 반환합니다.
     *
     * @param memberId 회원 id
     * @param pageable 페이지 정보
     * @return 승인된 친구 관계인 회원들
     */
    @Query("SELECT f.sender FROM Friend f WHERE f.receiver.id = :memberId AND f.friendRequestStatus = 'ACCEPTED'" +
            "UNION " +
            "SELECT f.receiver FROM Friend f WHERE f.sender.id = :memberId AND f.friendRequestStatus = 'ACCEPTED'")
    Page<Member> findAcceptedFriendByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    /**
     * 두 회원 중 하나가 sender이고, 다른 하나가 receiver인 경우와 그 반대의 경우를 모두 찾고, 그 중 승인된 친구 관계를 반환합니다.
     *
     * @param senderId 첫번째 회원 id
     * @param receiverId 두번째 회원 id
     * @return 승인된 친구 관계
     * */
    @Query("SELECT f FROM Friend f " +
            "WHERE f.receiver.id = :receiverId AND f.sender.id = :senderId AND f.friendRequestStatus = 'ACCEPTED'" +
            "UNION " +
            "SELECT f FROM Friend f " +
            "WHERE f.sender.id = :receiverId AND f.receiver.id = :senderId AND f.friendRequestStatus = 'ACCEPTED'")
    Optional<Friend> findAcceptedFriendByMembers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);


    /**
     * 두 회원 중 하나가 sender이고, 다른 하나가 receiver인 경우와 그 반대의 경우를 찾아 해당 친구 엔티티를 반환합니다.
     *
     * @param senderId 첫번째 회원 id
     * @param receiverId 두번째 회원 id
     * @return 친구 엔티티
     * */
    @Query("SELECT f FROM Friend f " +
            "WHERE f.receiver.id = :receiverId AND f.sender.id = :senderId " +
            "UNION " +
            "SELECT f FROM Friend f " +
            "WHERE f.sender.id = :receiverId AND f.receiver.id = :senderId")
    Optional<Friend> findByMembers(Long senderId, Long receiverId);
}
