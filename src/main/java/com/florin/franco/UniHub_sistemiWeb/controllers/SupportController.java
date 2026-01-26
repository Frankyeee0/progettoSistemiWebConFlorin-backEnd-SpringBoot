package com.florin.franco.UniHub_sistemiWeb.controllers;

import com.florin.franco.UniHub_sistemiWeb.dto.SupportRequest;
import com.florin.franco.UniHub_sistemiWeb.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @PostMapping("/report")
    public ResponseEntity<?> report(@RequestBody SupportRequest request) {
        try {
            supportService.handleSupportRequest(request);
            return ResponseEntity.ok("Segnalazione inviata");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
