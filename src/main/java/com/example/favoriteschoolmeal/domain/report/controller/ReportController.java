package com.example.favoriteschoolmeal.domain.report.controller;

import com.example.favoriteschoolmeal.domain.report.controller.dto.BlockRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.CreateReportRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportListResponse;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportResponse;
import com.example.favoriteschoolmeal.domain.report.service.ReportService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReportResponse> reportAdd(
            @Valid @RequestBody final CreateReportRequest request) {
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

    @PatchMapping("/{reportId}/block")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReportResponse> blockMemberWithReport(@PathVariable Long reportId,
            @Valid @RequestBody BlockRequest blockRequest) {
        ReportResponse response = reportService.blockMemberAndResolveReport(reportId, blockRequest);
        return ApiResponse.createSuccess(response);
    }


}
