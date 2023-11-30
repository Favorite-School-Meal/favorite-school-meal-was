package com.example.favoriteschoolmeal.domain.member.service;

import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.comment.repository.CommentRepository;
import com.example.favoriteschoolmeal.domain.friend.domain.Friend;
import com.example.favoriteschoolmeal.domain.friend.repository.FriendRepository;
import com.example.favoriteschoolmeal.domain.matching.domain.MatchingMember;
import com.example.favoriteschoolmeal.domain.matching.repository.MatchingMemberRepository;
import com.example.favoriteschoolmeal.domain.member.repository.MemberRepository;
import com.example.favoriteschoolmeal.domain.notification.domain.Notification;
import com.example.favoriteschoolmeal.domain.notification.repository.NotificationRepository;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.domain.report.domain.Report;
import com.example.favoriteschoolmeal.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 멤버 삭제를 담당하는 서비스입니다.
 * 멤버 객체와 관련된 모든 정보를 삭제합니다.
 * */
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

    public void deleteMember(Long memberId) {

        //멤버와 관련된 report 삭제
        deleteReport(memberId);

        //멤버와 관련된 notification 삭제
        deleteNotification(memberId);

        //멤버와 관련된 comment 삭제
        deleteComment(memberId);

        //멤버와 관련된 matchingMember 삭제
        deleteMatchingMember(memberId);

        //멤버와 관련된 matching삭제
        deleteMatching(memberId);

        //멤버와 관련된 post 삭제
        deletePost(memberId);

        //멤버와 관련된 friend 삭제
        deleteFriend(memberId);

        //마지막에 멤버 삭제
        memberRepository.deleteById(memberId);
    }

    private void deleteFriend(Long memberId) {
        //멤버가 보낸 모든 friend 조회
        List<Friend> sentFriends = friendRepository.findAllBySenderId(memberId);

        //멤버가 받은 모든 friend 조회
        List<Friend> receivedFriends = friendRepository.findAllByReceiverId(memberId);

        //friend 삭제
        friendRepository.deleteAll(sentFriends);
        friendRepository.deleteAll(receivedFriends);
    }

    private void deleteMatching(Long memberId) {

    }

    private void deleteNotification(Long memberId) {
        //멤버가 받거나 보낸 모든 notification 조회
        List<Notification> notifications = notificationRepository.findAllByMemberId(memberId);

        //notification 삭제
        notificationRepository.deleteAll(notifications);
    }

    private void deleteReport(Long memberId) {
        //멤버가 신고하거나 신고받은 모든 report 조회
        List<Report> reports = reportRepository.findAllByMemberId(memberId);

        //report 삭제
        reportRepository.deleteAll(reports);
    }

    private void deleteMatchingMember(Long memberId) {
        //멤버가 참여한 모든 matching 조회
        List<MatchingMember> matchingMembers = matchingMemberRepository.findAllByMemberId(memberId);

        //matchingMember 삭제
        matchingMemberRepository.deleteAllByMemberId(memberId);
    }

    private void deleteComment(Long memberId) {
        //멤버가 작성한 모든 comment 조회
        List<Comment> comments = commentRepository.findAllByMemberId(memberId);


        //각 comment에 대해 관련된 report 삭제(댓글 신고)
        comments.forEach(comment -> {
            reportRepository.deleteAllByCommentId(comment.getId());
        });

        //comment 삭제
        commentRepository.deleteAllByMemberId(memberId);


    }

    /**
     * 멤버와 관련된 Post를 삭제하는 메서드
     * */
    private void deletePost(Long memberId) {
        //멤버가 작성한 모든 post 조회
        List<Post> posts = postRepository.findAllByMemberId(memberId);


//        //각 post에 관련된 comment 제거
//        deleteCommentByPosts(posts);
//
//        //각 post에 관련된 matchingMember 제거
//        deleteMatchingMemberByPosts(posts);
//
//        //각 post에 관련된 report 제거
//        deleteReportByPosts(posts);

        //post 삭제
        postRepository.deleteAllByMemberId(memberId);
        //post에 관련된 matching은 cascade로 삭제됨
    }

    private void deleteReportByPosts(List<Post> posts) {
        //각 post에 대해 report 삭제(포스트 신고)
        posts.forEach(post -> {
            reportRepository.deleteAllByPostId(post.getId());
        });
    }

    private void deleteMatchingMemberByPosts(List<Post> posts) {
        //각 post에 대해 matchingMember 삭제
        posts.forEach(post -> {
            List<MatchingMember> matchingMembers = matchingMemberRepository.findAllByPostId(post.getId());
            matchingMemberRepository.deleteAll(matchingMembers);
        });
    }

    private void deleteCommentByPosts(List<Post> posts) {
        //각 post에 대해 comment 조회
        List<Comment> comments = commentRepository.findAllByPostId(post.getId());

        //각 comment에 대해 report 삭제(댓글 신고)
        comments.forEach(comment -> {
            reportRepository.deleteAllByCommentId(comment.getId());
        });

        //comments 삭제
        commentRepository.deleteAll(comments);
    }

    }
}
