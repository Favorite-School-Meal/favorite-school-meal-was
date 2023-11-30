package com.example.favoriteschoolmeal.domain.member.service;

import com.example.favoriteschoolmeal.domain.comment.repository.CommentRepository;
import com.example.favoriteschoolmeal.domain.friend.repository.FriendRepository;
import com.example.favoriteschoolmeal.domain.matching.repository.MatchingMemberRepository;
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
 * 멤버 삭제를 담당하는 서비스입니다. 멤버 객체와 관련된 모든 정보를 삭제합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberDeleteService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MatchingMemberRepository matchingMemberRepository;
    private final NotificationRepository notificationRepository;
    private final ReportRepository reportRepository;

    public void deleteMember() {

        Long memberId = getCurrentMemberId();

        // friend 객체의 receiverId 또는 senderId 필드가 memberId인 friend 모두 삭제
        deleteFriend(memberId);

        // report 객체의 reporterId 또는 reportedId 필드가 memberId인 report를 모두 삭제
        deleteReport(memberId);

        // notification 객체의 receiverId 또는 senderId가 memberId인 notification 모두 삭제
        deleteNotification(memberId);

        // comment 객체의 memberId가 memberId인 댓글 삭제 (사용자가 작성한 댓글 삭제)
        // post 객체의 postId가 comment 객체의 postId인 comment 모두 삭제 (사용자의 게시글에 달린 댓글 삭제)
        deleteComment(memberId);

        // matchingMember 객체의 memberId가 memberId인 matchingMember 모두 삭제
        deleteMatchingMember(memberId);

        // matching 객체의 postId가 Post 객체의 postId인 matching 모두 삭제
        deleteMatchingOnMemberPost(memberId);

        // post 객체의 memberId가 memberId인 모든 post 삭제
        deletePost(memberId);

        // Member 객체의 memberId가 memberId인 member 객체 삭제
        memberRepository.deleteById(memberId);
    }

    // friend 객체의 receiverId 또는 senderId 필드가 memberId인 friend 모두 삭제
    private void deleteFriend(Long memberId) {
        friendRepository.deleteBySenderIdOrReceiverId(memberId, memberId);
    }

    // report 객체의 reporterId 또는 reportedId 필드가 memberId인 report를 모두 삭제
    private void deleteReport(Long memberId) {
        reportRepository.deleteByReporterIdOrReportedMemberId(memberId, memberId);
    }

    // notification 객체의 receiverId 또는 senderId가 memberId인 notification 모두 삭제
    private void deleteNotification(Long memberId) {
        notificationRepository.deleteBySenderIdOrReceiverId(memberId, memberId);
    }

    private void deleteComment(Long memberId) {
        deleteCommentsByMember(memberId);
        deleteCommentsOnMemberPosts(memberId);
    }

    // comment 객체의 memberId가 memberId인 댓글 삭제 (사용자가 작성한 댓글 삭제)
    private void deleteCommentsByMember(Long memberId) {
        commentRepository.deleteByMemberId(memberId);
    }

    // post 객체의 postId가 comment 객체의 postId인 comment 모두 삭제 (사용자의 게시글에 달린 댓글 삭제)
    private void deleteCommentsOnMemberPosts(Long memberId) {
        findPostsByMemberId(memberId).forEach(
                post -> commentRepository.deleteByPostId(post.getId()));
    }

    // matchingMember 객체의 memberId가 memberId인 matchingMember 모두 삭제
    private void deleteMatchingMember(Long memberId) {
        matchingMemberRepository.deleteByMemberId(memberId);
    }

    // matching 객체의 postId가 Post 객체의 postId인 matching 모두 삭제
    private void deleteMatchingOnMemberPost(Long memberId) {
        findPostsByMemberId(memberId).forEach(
                post -> matchingMemberRepository.deleteByPostId(post.getId()));
    }

    /**
     * 멤버와 관련된 Post를 삭제하는 메서드
     */
    private void deletePost(Long memberId) {
        postRepository.deleteByMemberId(memberId);
    }

    // memberId로 해당 회원이 작성한 게시물 조회
    private List<Post> findPostsByMemberId(Long memberId) {
        return postRepository.findAllByMemberId(memberId);
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(
                () -> new PostException(PostExceptionType.MEMBER_NOT_FOUND));
    }
}
