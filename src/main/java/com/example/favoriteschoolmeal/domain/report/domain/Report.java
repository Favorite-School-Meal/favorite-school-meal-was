package com.example.favoriteschoolmeal.domain.report.domain;

import com.example.favoriteschoolmeal.domain.chat.domain.Chat;
import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.model.ReportType;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.global.common.Base;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Report extends Base {
    @Id
    @Column(name = "report_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id")
    private Member reportedMember;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_post_id")
    private Post reportedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_comment_id")
    private Comment reportedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_chat_id")
    private Chat reportedChat;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 300)
    private String content;

    @Column(name = "is_resolved", nullable = false)
    private Boolean isResolved;

    @Builder
    public Report(Member reporter, Member reportedMember, ReportType reportType, Post reportedPost, Comment reportedComment, Chat reportedChat, String title, String content, Boolean isResolved) {
        this.reporter = reporter;
        this.reportedMember = reportedMember;
        this.reportType = reportType;
        this.reportedPost = reportedPost;
        this.reportedComment = reportedComment;
        this.reportedChat = reportedChat;
        this.title = title;
        this.content = content;
        this.isResolved = isResolved;
    }

    public void resolveReport() {
        this.isResolved = true;
    }
}
