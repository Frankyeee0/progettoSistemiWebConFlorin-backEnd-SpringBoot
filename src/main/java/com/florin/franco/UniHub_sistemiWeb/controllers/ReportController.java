package com.florin.franco.UniHub_sistemiWeb.controllers;

import com.florin.franco.UniHub_sistemiWeb.api.dto.ReportCreateRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.ReportUpdateRequest;
import com.florin.franco.UniHub_sistemiWeb.service.ReportService;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportStatus;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportTargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReportCreateRequest request) {
        try {
            return ResponseEntity.ok(reportService.createReport(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) ReportTargetType type
    ) {
        try {
            return ResponseEntity.ok(reportService.listReports(status, type));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody ReportUpdateRequest request) {
        try {
            return ResponseEntity.ok(reportService.updateStatus(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
