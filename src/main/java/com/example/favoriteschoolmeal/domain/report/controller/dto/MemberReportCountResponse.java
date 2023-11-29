package com.example.favoriteschoolmeal.domain.report.controller.dto;

public record MemberReportCountResponse(
        Long reportCount
) {
    public static MemberReportCountResponse from(Long count) {
        return new MemberReportCountResponse(
                count
        );
    }
}
