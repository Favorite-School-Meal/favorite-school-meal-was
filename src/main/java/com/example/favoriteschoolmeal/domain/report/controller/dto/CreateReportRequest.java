package com.example.favoriteschoolmeal.domain.report.controller.dto;

import com.example.favoriteschoolmeal.domain.model.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(
        @NotNull
        Long reportedMemberId,
        @NotBlank
        String content,
        @NotBlank
        ReportType reportType,

        Long postId,
        Long commentId,
        Long chatId
) {
}