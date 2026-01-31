package com.florin.franco.UniHub_sistemiWeb.controllers;

import com.florin.franco.UniHub_sistemiWeb.api.dto.ReportCreateRequest;
import com.florin.franco.UniHub_sistemiWeb.api.dto.ReportUpdateRequest;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.service.ReportService;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportStatus;
import com.florin.franco.UniHub_sistemiWeb.utils.ReportTargetType;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private AppUserRepository userRepository;

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
            requireSuperAdmin(1L);
            return ResponseEntity.ok(reportService.listReports(status, type));
        } catch (RuntimeException e) {
            if (e instanceof org.springframework.web.server.ResponseStatusException) {
                throw e;
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody ReportUpdateRequest request) {
        try {
            requireSuperAdmin(1L);
            return ResponseEntity.ok(reportService.updateStatus(id, request));
        } catch (RuntimeException e) {
            if (e instanceof org.springframework.web.server.ResponseStatusException) {
                throw e;
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void requireSuperAdmin(Long actorId) {
        if (actorId == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Permesso negato");
        }
        AppUser user = userRepository.findById(actorId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.FORBIDDEN, "Permesso negato"));
        if (user.getRole() != Ruolo.SUPERADMIN) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Permesso negato");
        }
    }
}
