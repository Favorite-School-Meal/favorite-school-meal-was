package com.example.favoriteschoolmeal.domain.report.controller.dto;

import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.model.ReportType;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.report.domain.Report;
import com.example.favoriteschoolmeal.domain.chat.domain.Chat;
import com.example.favoriteschoolmeal.domain.report.exception.ReportException;
import com.example.favoriteschoolmeal.domain.report.exception.ReportExceptionType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public record ReportResponse(
        Long id,

        String elapsedDays,
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
                getElapsedDays(report),
                report.getReportedMember().getId(),
                report.getReportedMember().getNickname(),
                report.getReporter().getId(),
                report.getReporter().getNickname(),
                report.getReportType().toString(),
                report.getTitle(),
                report.getContent(),
                Optional.ofNullable(report.getReportedPost()).map(Post::getId).orElse(null),
                Optional.ofNullable(report.getReportedComment()).map(Comment::getId).orElse(null),
                Optional.ofNullable(report.getReportedChat()).map(Chat::getId).orElse(null)
        );

    }

    private static String getElapsedDays(Report report) {
        long days = Duration.between(report.getCreatedAt(), LocalDateTime.now()).toDays();
        if (days == 0) {
            return "오늘";
        } else if (days == 1) {
            return "어제";
        } else {
            return days + "일 전";
        }

    }
}
