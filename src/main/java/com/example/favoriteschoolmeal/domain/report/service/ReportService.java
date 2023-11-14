package com.example.favoriteschoolmeal.domain.report.service;

import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.report.controller.dto.CreateReportRequest;
import com.example.favoriteschoolmeal.domain.report.controller.dto.ReportResponse;
import com.example.favoriteschoolmeal.domain.report.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportResponse addReport(CreateReportRequest request, Member member) {
      log.info("member.name : {}", member.getFullName());
      return null;
    }
}
