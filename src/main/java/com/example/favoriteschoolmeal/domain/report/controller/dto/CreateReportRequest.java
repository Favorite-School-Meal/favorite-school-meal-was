package com.example.favoriteschoolmeal.domain.report.controller.dto;

import com.example.favoriteschoolmeal.domain.model.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReportRequest(
        @NotNull
        Long reportedMemberId,

        String content,
        @NotNull
        ReportType reportType,

        Long postId,
        Long commentId,
        Long chatId
) {

}
