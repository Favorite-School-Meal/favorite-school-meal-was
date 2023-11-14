package com.example.favoriteschoolmeal.domain.report.repository;


import com.example.favoriteschoolmeal.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
