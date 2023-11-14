package com.example.favoriteschoolmeal.domain.report.controller.dto;

import com.example.favoriteschoolmeal.domain.report.domain.Report;

public record ReportResponse(
        Long id,
        Long reportedMemberId,
        String reportedMemberNickname,

        Long reporterMemberId,
        String reporterMemberNickname,

        String reportType,//POST, COMMENT, CHAT, PROFILE
        String title,//게시물 또는 댓글 신고인 경우 해당 게시물 또는 댓글 미리보기 제공
        String content,
        Long postId,
        Long commentId,
        Long chatId

        //TODO: 신고 횟수 제공
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getReportedMember().getId(),
                report.getReportedMember().getNickname(),
                report.getReporter().getId(),
                report.getReporter().getNickname(),
                report.getReportType().toString(),
                //title
                report.getReportedPost() != null ? report.getReportedPost().getTitle() : report.getReportedComment() != null ? report.getReportedComment().getContent() : null,
                report.getContent(),
                report.getReportedPost() != null ? report.getReportedPost().getId() : null,
                report.getReportedComment() != null ? report.getReportedComment().getId() : null,
                report.getReportedChat() != null ? report.getReportedChat().getId() : null
        );
    }
}
