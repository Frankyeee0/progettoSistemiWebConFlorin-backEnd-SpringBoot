package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.entity.EmailLog;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EmailLogRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.EmailStatus;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@RestController
@RequestMapping({"/api/email-logs", "/api/email-history"})
public class EmailLogController {

    @Autowired
    private EmailLogRepository emailLogRepository;
    @Autowired
    private AppUserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<EmailLog>> list(
            @RequestParam Long actorId,
            @RequestParam(required = false) EmailStatus status,
            @RequestParam(required = false) String type) {
        AppUser actor = requireSuperAdmin(actorId);
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

    private AppUser requireSuperAdmin(Long actorId) {
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
        return user;
    }
}
