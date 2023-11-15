package com.example.favoriteschoolmeal.domain.report.service;

import com.example.favoriteschoolmeal.domain.chat.domain.Chat;
import com.example.favoriteschoolmeal.domain.chat.service.ChatService;
import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.comment.service.CommentService;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import com.example.favoriteschoolmeal.domain.report.controller.dto.CreateReportRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportResponse;
import com.example.favoriteschoolmeal.domain.report.domain.Report;
import com.example.favoriteschoolmeal.domain.report.exception.ReportCreationException;
import com.example.favoriteschoolmeal.domain.report.exception.ReportExceptionType;
import com.example.favoriteschoolmeal.domain.report.repository.ReportRepository;
import com.example.favoriteschoolmeal.global.security.userdetails.CustomUserDetails;
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

    public ReportResponse addReport(CreateReportRequest request, CustomUserDetails userDetails) {
        Member reporter = userDetails.getMember();

        Report report = createReport(request, reporter);
        reportRepository.save(report);
        return ReportResponse.from(report);
    }

    private Report createReport(CreateReportRequest request, Member reporter) {

        return switch (request.reportType()) {
            case POST -> buildPostReport(request, reporter);
            case PROFILE -> buildProfileReport(request, reporter);
            case COMMENT -> buildCommentReport(request, reporter);
            case CHAT -> buildChatReport(request, reporter);
            default -> throw new ReportCreationException(ReportExceptionType.REPORT_TYPE_NOT_FOUND);
        };

    }

    private Report buildChatReport(CreateReportRequest request, Member reporter) {
        Chat chat = chatService.findChatById(request.chatId())
                .orElseThrow(() -> new ReportCreationException(ReportExceptionType.CHAT_NOT_FOUND));

        Member reportedMember = memberService.findMemberById(request.reportedMemberId())
                .orElseThrow(() -> new ReportCreationException(ReportExceptionType.MEMBER_NOT_FOUND));
        return Report.builder()
                .reporter(reporter)
                .reportedMember(reportedMember)
                .reportedChat(chat)
                .reportType(request.reportType())
                .content(request.content())
                .isResolved(false)
                .build();
    }

    private Report buildCommentReport(CreateReportRequest request, Member reporter) {
        Comment comment = commentService.findCommentById(request.commentId())
                .orElseThrow(() -> new ReportCreationException(ReportExceptionType.COMMENT_NOT_FOUND));
        return Report.builder()
                .reporter(reporter)
                .reportedMember(comment.getMember())
                .reportedComment(comment)
                .reportType(request.reportType())
                .content(request.content())
                .isResolved(false)
                .build();
    }

    private Report buildProfileReport(CreateReportRequest request, Member reporter) {
        Member reportedMember = memberService.findMemberById(request.reportedMemberId())
                .orElseThrow(() -> new ReportCreationException(ReportExceptionType.MEMBER_NOT_FOUND));

        return Report.builder()
                .reporter(reporter)
                .reportedMember(reportedMember)
                .reportType(request.reportType())
                .content(request.content())
                .isResolved(false)
                .build();
    }

    private Report buildPostReport(CreateReportRequest request, Member reporter) {
        Post post = postService.findPostById(request.postId())
                .orElseThrow(() -> new ReportCreationException(ReportExceptionType.POST_NOT_FOUND));

        return Report.builder()
                .reporter(reporter)
                .reportedMember(post.getMember())
                .reportedPost(post)
                .reportType(request.reportType())
                .content(request.content())
                .isResolved(false)
                .build();
    }
}
