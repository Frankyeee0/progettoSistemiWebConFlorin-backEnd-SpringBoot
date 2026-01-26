package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Report;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportStatus;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportTargetType;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatus(ReportStatus status);
    List<Report> findByTargetType(ReportTargetType targetType);
    List<Report> findByStatusAndTargetType(ReportStatus status, ReportTargetType targetType);
}
