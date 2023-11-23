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
import com.example.favoriteschoolmeal.domain.report.controller.dto.BlockRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.CreateReportRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportListResponse;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportResponse;
import com.example.favoriteschoolmeal.domain.report.domain.Report;
import com.example.favoriteschoolmeal.domain.report.exception.ReportException;
import com.example.favoriteschoolmeal.domain.report.exception.ReportExceptionType;
import com.example.favoriteschoolmeal.domain.report.repository.ReportRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        verifyRoleUserOrAdmin();
        Member reporter = getMemberOrThrow(getCurrentMemberId());

        Report report = createReport(request, reporter);
        reportRepository.save(report);
        return ReportResponse.from(report);
    }

    @Transactional(readOnly = true)
    public ReportListResponse findAllReportByIsResolvedFalse(Pageable pageable) {
        verifyRoleAdmin();
        Page<ReportResponse> reportResponses = reportRepository.findAllByIsResolvedFalse(pageable)
                .map(ReportResponse::from);
        return ReportListResponse.from(reportResponses);
    }

    @Transactional(readOnly = true)
    public ReportResponse findReport(Long reportId) {
        verifyRoleAdmin();
        Report report = getReportOrThrow(reportId);
        return ReportResponse.from(report);
    }

    public ReportResponse blockMemberAndResolveReport(Long reportId, BlockRequest blockRequest) {
        verifyRoleAdmin();
        Report report = getReportOrThrow(reportId);
        checkNotResolved(report);
        Member reportedMember = getReportedMemberOrThrow(report);
        memberService.blockMember(reportedMember, blockRequest.blockHours());
        report.resolveReport();
        return ReportResponse.from(report);
    }

    private static void checkNotResolved(Report report) {
        if (report.getIsResolved()) {
            throw new ReportException(ReportExceptionType.ALREADY_RESOLVED);
        }
    }

    private Member getReportedMemberOrThrow(Report report) {
        if (report.getReportedMember() == null) {
            throw new ReportException(ReportExceptionType.MEMBER_NOT_FOUND);
        }
        return report.getReportedMember();
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

    private String getTitle(Object entity) {
        if (entity == null) {
            return null;
        }

        String content;
        String titleSuffix;
        if (entity instanceof Member) {
            content = ((Member) entity).getNickname();
            titleSuffix = "님의 프로필";
        } else if (entity instanceof Chat) {
            content = ((Chat) entity).getId().toString();
            titleSuffix = "번 채팅방";
        } else if (entity instanceof Comment) {
            content = ((Comment) entity).getContent();
            titleSuffix = " 댓글";
        } else if (entity instanceof Post) {
            content = ((Post) entity).getTitle();
            titleSuffix = " 게시물";
        } else {
            return null;
        }

        int maxLength = 20;
        String truncatedContent = content.substring(0, Math.min(content.length(), maxLength));
        if (content.length() > maxLength) {
            truncatedContent += "...";
        }

        return truncatedContent + titleSuffix;
    }

    private Report buildChatReport(CreateReportRequest request, Member reporter) {
        Chat chat = getChatOrThrow(request.chatId());
        Member reportedMember = getMemberOrThrow(request.reportedMemberId());
        return ReportBuilder(reporter, reportedMember, request.reportType(), null, null, chat, getTitle(chat), request.content());
    }

    private Report buildCommentReport(CreateReportRequest request, Member reporter) {
        Comment comment = getCommentOrThrow(request.commentId());
        return ReportBuilder(reporter, comment.getMember(), request.reportType(), null, comment, null, getTitle(comment), request.content());
    }

    private Report buildProfileReport(CreateReportRequest request, Member reporter) {
        Member reportedMember = getMemberOrThrow(request.reportedMemberId());
        return ReportBuilder(reporter, reportedMember, request.reportType(), null, null, null, getTitle(reportedMember), request.content());
    }

    private Report buildPostReport(CreateReportRequest request, Member reporter) {
        Post post = getPostOrThrow(request.postId());
        return ReportBuilder(reporter, post.getMember(), request.reportType(), post, null, null, getTitle(post), request.content());
    }

    private Post getPostOrThrow(Long postId) {
        return postService.findPostOptionally(postId)
                .orElseThrow(() -> new ReportException(ReportExceptionType.POST_NOT_FOUND));
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(() -> new ReportException(ReportExceptionType.MEMBER_NOT_FOUND));
    }

    private void verifyRoleUserOrAdmin() {
        SecurityUtils.checkUserOrAdminOrThrow(() -> new ReportException(ReportExceptionType.UNAUTHORIZED_ACCESS));
//    ("ROLE_USER", () -> new ReportException(ReportExceptionType.UNAUTHORIZED_ACCESS));
    }

    private Report ReportBuilder(Member reporter, Member reportedMember, ReportType reportType, Post reportedPost, Comment reportedComment, Chat reportedChat, String title, String content) {
        return Report.builder()
                .reporter(reporter)
                .reportedMember(reportedMember)
                .reportType(reportType)
                .reportedPost(reportedPost)
                .reportedComment(reportedComment)
                .reportedChat(reportedChat)
                .title(title)
                .content(content)
                .isResolved(false)
                .build();
    }

    private void verifyRoleAdmin() {
        SecurityUtils.checkAdminOrThrow(() -> new ReportException(ReportExceptionType.UNAUTHORIZED_ACCESS));
    }

    private Report getReportOrThrow(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportExceptionType.REPORT_NOT_FOUND));
    }


}
