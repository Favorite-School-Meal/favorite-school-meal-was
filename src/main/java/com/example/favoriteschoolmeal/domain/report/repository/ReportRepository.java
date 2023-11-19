package com.example.favoriteschoolmeal.domain.report.repository;


import com.example.favoriteschoolmeal.domain.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE r.isResolved = false ORDER BY r.createdAt")
    Page<Report> findAllByIsResolvedFalse(Pageable pageable);
}
