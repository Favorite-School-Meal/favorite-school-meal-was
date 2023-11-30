package com.example.favoriteschoolmeal.domain.member.service;

import com.example.favoriteschoolmeal.domain.comment.repository.CommentRepository;
import com.example.favoriteschoolmeal.domain.friend.repository.FriendRepository;
import com.example.favoriteschoolmeal.domain.matching.repository.MatchingMemberRepository;
import com.example.favoriteschoolmeal.domain.matching.repository.MatchingRepository;
import com.example.favoriteschoolmeal.domain.member.exception.MemberException;
import com.example.favoriteschoolmeal.domain.member.exception.MemberExceptionType;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.notification.repository.NotificationRepository;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.exception.PostException;
import com.example.favoriteschoolmeal.domain.post.exception.PostExceptionType;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.domain.report.repository.ReportRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberDeleteService 클래스는 멤버 삭제를 담당하는 서비스입니다. 멤버 객체와 관련된 모든 정보를 삭제합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberDeleteService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MatchingRepository matchingRepository;
    private final MatchingMemberRepository matchingMemberRepository;
    private final NotificationRepository notificationRepository;
    private final ReportRepository reportRepository;

    /**
     * 현재 로그인한 멤버의 정보를 기반으로 멤버 삭제를 수행합니다.
     */
    public void deleteMember() {

        // 현재 로그인한 멤버의 ID를 가져옵니다.
        Long memberId = getCurrentMemberId();

        // Notification 객체의 receiverId 또는 senderId가 memberId인 모든 Notification을 삭제합니다.
        deleteNotification(memberId);

        // Friend 객체의 receiverId 또는 senderId 필드가 memberId인 모든 Friend를 삭제합니다.
        deleteFriend(memberId);

        // Report 객체의 reporterId 또는 reportedMemberId 필드가 memberId인 모든 Report를 삭제합니다.
        deleteReport(memberId);

        // Comment 객체의 memberId가 memberId인 댓글을 삭제합니다. (사용자가 작성한 댓글 삭제)
        // 또한, Post 객체의 postId가 Comment 객체의 postId인 댓글도 삭제합니다. (사용자의 게시글에 달린 댓글 삭제)
        deleteComment(memberId);

        // MatchingMember 객체의 memberId가 memberId인 모든 MatchingMember를 삭제합니다.
        deleteMatchingMember(memberId);

        // Post 객체의 memberId가 memberId인 모든 Post를 삭제합니다.
        deletePost(memberId);

        // Member 객체의 memberId가 memberId인 Member를 삭제합니다.
        memberRepository.deleteById(memberId);
    }

    /**
     * Friend 객체의 receiverId 또는 senderId 필드가 memberId인 Friend를 모두 삭제합니다.
     * @param memberId 삭제할 멤버의 ID
     */
    private void deleteFriend(Long memberId) {
        friendRepository.deleteBySenderIdOrReceiverId(memberId, memberId);
    }

    /**
     * Report 객체의 reporterId 또는 reportedId 필드가 memberId인 Report를 모두 삭제합니다.
     * @param memberId 삭제할 멤버의 ID
     */
    private void deleteReport(Long memberId) {
        reportRepository.deleteByReporterIdOrReportedMemberId(memberId, memberId);
    }

    /**
     * Notification 객체의 receiverId 또는 senderId가 memberId인 Notification을 모두 삭제합니다.
     * @param memberId 삭제할 멤버의 ID
     */
    private void deleteNotification(Long memberId) {
        notificationRepository.deleteBySenderIdOrReceiverId(memberId, memberId);
    }

    /**
     * Comment 객체의 memberId가 memberId인 댓글을 삭제합니다. (사용자가 작성한 댓글 삭제)
     * 또한, Comment 객체의 postId가 Comment 객체의 postId인 댓글도 삭제합니다. (사용자의 게시글에 달린 댓글 삭제)
     * @param memberId 삭제할 멤버의 ID
     */
    private void deleteComment(Long memberId) {
        deleteCommentsByMember(memberId);
        deleteCommentsOnMemberPosts(memberId);
    }

    /**
     * Comment 객체의 memberId가 memberId인 댓글을 삭제합니다. (사용자가 작성한 댓글 삭제)
     * @param memberId 삭제할 멤버의 ID
     */
    private void deleteCommentsByMember(Long memberId) {
        commentRepository.deleteByMemberId(memberId);
    }

    /**
     * Post 객체의 postId가 Comment 객체의 postId인 댓글을 삭제합니다. (사용자의 게시글에 달린 댓글 삭제)
     * @param memberId 삭제할 멤버의 ID
     */
    private void deleteCommentsOnMemberPosts(Long memberId) {
        findPostsByMemberId(memberId).forEach(
                post -> commentRepository.deleteByPostId(post.getId()));
    }

    /**
     * MatchingMember 객체의 memberId가 memberId인 MatchingMember를 모두 삭제합니다.
     * @param memberId 삭제할 멤버의 ID
     */
    private void deleteMatchingMember(Long memberId) {
        matchingMemberRepository.deleteByMemberId(memberId);
    }

    /**
     * 멤버와 관련된 Post를 삭제하는 메서드
     * @param memberId 삭제할 멤버의 ID
     */
    private void deletePost(Long memberId) {
        postRepository.deleteByMemberId(memberId);
    }

    /**
     * memberId로 해당 회원이 작성한 게시물을 조회합니다.
     * @param memberId 조회할 멤버의 ID
     * @return 해당 멤버가 작성한 게시물 목록
     */
    private List<Post> findPostsByMemberId(Long memberId) {
        return postRepository.findAllByMemberId(memberId);
    }

    /**
     * 현재 로그인한 멤버의 ID를 가져오는 메서드
     * 만약 멤버를 찾을 수 없는 경우, MemberException을 발생시킵니다.
     * @return 현재 로그인한 멤버의 ID
     */
    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(
                () -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
