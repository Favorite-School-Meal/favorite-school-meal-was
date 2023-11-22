package com.example.favoriteschoolmeal.domain.comment.service;

import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.comment.exception.CommentException;
import com.example.favoriteschoolmeal.domain.comment.exception.CommentExceptionType;
import com.example.favoriteschoolmeal.domain.comment.repository.CommentRepository;
import com.example.favoriteschoolmeal.domain.comment.service.dto.CreateCommentCommand;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.exception.PostException;
import com.example.favoriteschoolmeal.domain.post.exception.PostExceptionType;
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MemberService memberService;

    public CommentService(final CommentRepository commentRepository, final PostService postService,
            final MemberService memberService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.memberService = memberService;
    }

    public Comment addComment(final CreateCommentCommand command) {
        verifyUserOrAdmin();
        final Post post = getPostOrThrow(command.postId());
        final Member member = getMemberOrThrow(getCurrentMemberId());
        final Comment comment = createComment(command, post, member);
        return commentRepository.save(comment);
    }

    private Comment createComment(CreateCommentCommand command, Post post, Member member) {
        return Comment.builder()
                .content(command.content())
                .post(post)
                .member(member)
                .build();
    }

    private Post getPostOrThrow(final Long postId) {
        return postService.findPostOptionally(postId)
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
}
