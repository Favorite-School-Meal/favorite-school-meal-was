package com.example.favoriteschoolmeal.domain.report.controller.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record ReportListResponse(
        List<ReportResponse> content,
        int currentPage,
        int totalPages,
        long totalElements
) {

    public static ReportListResponse from(Page<ReportResponse> page) {
        return new ReportListResponse(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements());
    }
}
