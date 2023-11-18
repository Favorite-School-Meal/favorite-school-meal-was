package com.example.favoriteschoolmeal.domain.report.service;

import com.example.favoriteschoolmeal.domain.chat.domain.Chat;
import com.example.favoriteschoolmeal.domain.chat.service.ChatService;
import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.comment.service.CommentService;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.ReportType;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import com.example.favoriteschoolmeal.domain.report.controller.dto.CreateReportRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportResponse;
import com.example.favoriteschoolmeal.domain.report.domain.Report;
import com.example.favoriteschoolmeal.domain.report.exception.ReportException;
import com.example.favoriteschoolmeal.domain.report.exception.ReportExceptionType;
import com.example.favoriteschoolmeal.domain.report.repository.ReportRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;
    private final ChatService chatService;

    public ReportResponse addReport(CreateReportRequest request) {
        verifyRoleUser();
        Member reporter = getMemberOrThrow(getCurrentMemberId());

        Report report = createReport(request, reporter);
        reportRepository.save(report);
        return ReportResponse.from(report);
    }

    private Report createReport(CreateReportRequest request, Member reporter) {

        if (request.reportType().equals(ReportType.PROFILE)) {
            return buildProfileReport(request, reporter);
        } else if (request.reportType().equals(ReportType.POST)) {
            return buildPostReport(request, reporter);
        } else if (request.reportType().equals(ReportType.COMMENT)) {
            return buildCommentReport(request, reporter);
        } else if (request.reportType().equals(ReportType.CHAT)) {
            return buildChatReport(request, reporter);
        } else {
            throw new ReportException(ReportExceptionType.REPORT_TYPE_NOT_FOUND);
        }

    }


    private Chat getChatOrThrow(Long chatId) {
        return chatService.findChatOptionally(chatId)
                .orElseThrow(() -> new ReportException(ReportExceptionType.CHAT_NOT_FOUND));
    }

    private Member getMemberOrThrow(Long memberId) {
        return memberService.findMemberOptionally(memberId)
                .orElseThrow(() -> new ReportException(ReportExceptionType.MEMBER_NOT_FOUND));
    }

    private Comment getCommentOrThrow(Long commentId) {
        return commentService.findCommentOptionally(commentId)
                .orElseThrow(() -> new ReportException(ReportExceptionType.COMMENT_NOT_FOUND));
    }

    private Report buildChatReport(CreateReportRequest request, Member reporter) {
        Chat chat = getChatOrThrow(request.chatId());

        Member reportedMember = getMemberOrThrow(request.reportedMemberId());

        return ReportBuilder(reporter, reportedMember, request.reportType(), null, null, chat, request.content());
    }

    private Report buildCommentReport(CreateReportRequest request, Member reporter) {
        Comment comment = getCommentOrThrow(request.commentId());

        return ReportBuilder(reporter, comment.getMember(), request.reportType(), null, comment, null, request.content());
    }

    private Report buildProfileReport(CreateReportRequest request, Member reporter) {
        Member reportedMember = getMemberOrThrow(request.reportedMemberId());

        return ReportBuilder(reporter, reportedMember, request.reportType(), null, null, null, request.content());
    }

    private Report buildPostReport(CreateReportRequest request, Member reporter) {
        Post post = getPostOrThrow(request.postId());
        return ReportBuilder(reporter, post.getMember(), request.reportType(), post, null, null, request.content());
    }

    private Post getPostOrThrow(Long postId) {
        return postService.findPostOptionally(postId)
                .orElseThrow(() -> new ReportException(ReportExceptionType.POST_NOT_FOUND));
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(() -> new ReportException(ReportExceptionType.MEMBER_NOT_FOUND));
    }

    private void verifyRoleUser() {
        SecurityUtils.checkUserAuthority("ROLE_USER", () -> new ReportException(ReportExceptionType.UNAUTHORIZED_ACCESS));
    }

    private Report ReportBuilder(Member reporter, Member reportedMember, ReportType reportType, Post reportedPost, Comment reportedComment, Chat reportedChat, String content) {
        return Report.builder()
                .reporter(reporter)
                .reportedMember(reportedMember)
                .reportType(reportType)
                .reportedPost(reportedPost)
                .reportedComment(reportedComment)
                .reportedChat(reportedChat)
                .content(content)
                .isResolved(false)
                .build();
    }
}
