package com.example.favoriteschoolmeal.domain.report.controller;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.report.controller.dto.CreateReportRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportResponse;
import com.example.favoriteschoolmeal.domain.report.service.ReportService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import com.example.favoriteschoolmeal.global.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ApiResponse<ReportResponse> reportAdd(@RequestBody final CreateReportRequest request,
                                                 @AuthenticationPrincipal Member member){
        ReportResponse response = reportService.addReport(request,member);
        return ApiResponse.createSuccess(response);
    }

}
