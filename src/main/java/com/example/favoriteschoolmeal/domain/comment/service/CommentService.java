package com.example.favoriteschoolmeal.domain.comment.service;

import com.example.favoriteschoolmeal.domain.comment.controller.dto.CommentResponse;
import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.comment.exception.CommentException;
import com.example.favoriteschoolmeal.domain.comment.exception.CommentExceptionType;
import com.example.favoriteschoolmeal.domain.comment.repository.CommentRepository;
import com.example.favoriteschoolmeal.domain.comment.service.dto.CreateCommentCommand;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.notification.service.NotificationService;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.exception.PostException;
import com.example.favoriteschoolmeal.domain.post.exception.PostExceptionType;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final NotificationService notificationService;

    public Comment addComment(final CreateCommentCommand command) {
        verifyUserOrAdmin();
        final Post post = getPostOrThrow(command.postId());
        final Member member = getMemberOrThrow(getCurrentMemberId());
        final Comment comment = createComment(command, post, member);
        final Comment savedComment = commentRepository.save(comment);
        notificationService.createNotification(post.getId(), post.getMember().getId(),
                NotificationType.COMMENT_POSTED);
        return savedComment;
    }

    public Comment modifyComment(final Long commentId, final CreateCommentCommand command) {
        verifyUserOrAdmin();
        final Long currentMemberId = getCurrentMemberId();

        final Comment comment = getCommentOrThrow(commentId);
        verifyCommentOwner(comment.getMember().getId(), currentMemberId);

        comment.modifyContent(command.content());
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllComment(final Long postId) {
        final Post post = getPostOrThrow(postId);
        return commentRepository.findAllByPost(post, Sort.by("createdAt").ascending());
    }

    public void removeComment(final Long postId, final Long commentId) {
        verifyUserOrAdmin();
        getPostOrThrow(postId);
        final Comment comment = getCommentOrThrow(commentId);
        verifyCommentOwner(comment.getMember().getId(), getCurrentMemberId());
        commentRepository.delete(comment);
    }

    public int countCommentByPostId(final Long postId) {
        final Post post = getPostOrThrow(postId);
        return commentRepository.countByPost(post);
    }

    public List<CommentResponse> createCommentResponseList(Post post) {
        List<Comment> comments = findAllComment(post.getId());
        return CommentResponse.listFrom(comments);
    }

    public void removeCommentsByPost(final Post post) {
        commentRepository.deleteAll(commentRepository.findAllByPost(post));
    }

    public Optional<Comment> findCommentOptionally(Long commentId) {
        return commentRepository.findById(commentId);
    }

    private Comment createComment(CreateCommentCommand command, Post post, Member member) {
        return Comment.builder()
                .content(command.content())
                .post(post)
                .member(member)
                .build();
    }

    private Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.COMMENT_NOT_FOUND));
    }

    private Post getPostOrThrow(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.POST_NOT_FOUND));
    }

    private Member getMemberOrThrow(final Long memberId) {
        return memberService.findMemberOptionally(memberId)
                .orElseThrow(() -> new CommentException(CommentExceptionType.MEMBER_NOT_FOUND));
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(
                () -> new CommentException(CommentExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyUserOrAdmin() {
        SecurityUtils.checkUserOrAdminOrThrow(
                () -> new PostException(PostExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyCommentOwner(final Long commentOwnerId, final Long currentMemberId) {
        if (!commentOwnerId.equals(currentMemberId) && !SecurityUtils.isAdmin()) {
            throw new CommentException(CommentExceptionType.UNAUTHORIZED_ACCESS);
        }
    }
}
