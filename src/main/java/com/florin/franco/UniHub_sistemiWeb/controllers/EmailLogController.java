package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.entity.EmailLog;
import com.florin.franco.UniHub_sistemiWeb.repository.EmailLogRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.EmailStatus;

@RestController
@RequestMapping("/api/email-logs")
public class EmailLogController {

    @Autowired
    private EmailLogRepository emailLogRepository;

    @GetMapping
    public ResponseEntity<List<EmailLog>> list(
            @RequestParam(required = false) EmailStatus status,
            @RequestParam(required = false) String type) {
        if (status != null && type != null) {
            return ResponseEntity.ok(emailLogRepository.findTop200ByStatusAndTypeOrderByCreatedAtDesc(status, type));
        }
        if (status != null) {
            return ResponseEntity.ok(emailLogRepository.findTop200ByStatusOrderByCreatedAtDesc(status));
        }
        if (type != null && !type.isBlank()) {
            return ResponseEntity.ok(emailLogRepository.findTop200ByTypeOrderByCreatedAtDesc(type));
        }
        return ResponseEntity.ok(emailLogRepository.findTop200ByOrderByCreatedAtDesc());
    }
}
