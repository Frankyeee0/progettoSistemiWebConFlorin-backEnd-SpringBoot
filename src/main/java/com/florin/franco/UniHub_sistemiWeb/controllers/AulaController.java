package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.dto.AulaDto;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.service.AulaService;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@RestController
@RequestMapping("/api/aule")
public class AulaController {

    @Autowired
    private AulaService aulaService;

    @Autowired
    private AppUserRepository userRepository;

    @GetMapping
    public List<AulaDto> list() {
        return aulaService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long actorId, @RequestBody AulaDto payload) {
        try {
            requireAdmin(actorId);
            return ResponseEntity.ok(aulaService.create(payload));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam Long actorId,
            @RequestBody AulaDto payload
    ) {
        try {
            requireAdmin(actorId);
            return ResponseEntity.ok(aulaService.update(id, payload));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long actorId) {
        try {
            requireAdmin(actorId);
            aulaService.delete(id);
            return ResponseEntity.ok("Eliminata");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void requireAdmin(Long actorId) {
        if (actorId == null) {
            throw new RuntimeException("Attore mancante");
        }
        AppUser user = userRepository.findById(actorId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (user.getRole() != Ruolo.ADMIN) {
            throw new RuntimeException("Permesso negato");
        }
    }
}
