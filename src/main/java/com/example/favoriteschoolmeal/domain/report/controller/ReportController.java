package com.example.favoriteschoolmeal.domain.report.controller;

import com.example.favoriteschoolmeal.domain.report.controller.dto.CreateReportRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportListResponse;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportResponse;
import com.example.favoriteschoolmeal.domain.report.service.ReportService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReportResponse> reportAdd(@RequestBody final CreateReportRequest request) {
        ReportResponse response = reportService.addReport(request);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReportListResponse> reportListByIsResolvedFalse(Pageable pageable) {
        ReportListResponse response = reportService.findAllReportByIsResolvedFalse(pageable);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/{reportId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReportResponse> reportDetails(@PathVariable Long reportId) {
        ReportResponse response = reportService.findReport(reportId);
        return ApiResponse.createSuccess(response);
    }

    @PatchMapping("/{reportId}/complete")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReportResponse> reportComplete(@PathVariable Long reportId) {
        ReportResponse response = reportService.resolveReport(reportId);
        return ApiResponse.createSuccess(response);
    }


}
