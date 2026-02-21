package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.dto.LezioneDto;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.service.LezioneService;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@RestController
@RequestMapping("/api/lezioni")
public class LezioneController {

    @Autowired
    private LezioneService lezioneService;

    @Autowired
    private AppUserRepository userRepository;

    @GetMapping
    public List<LezioneDto> list(
            @RequestParam(required = false) Long materiaId,
            @RequestParam(required = false) Long aulaId,
            @RequestParam(required = false) String giorno
    ) {
        return lezioneService.getAll(materiaId, aulaId, giorno);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long actorId, @RequestBody LezioneDto payload) {
        try {
            requireAdmin(actorId);
            return ResponseEntity.ok(lezioneService.create(payload));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam Long actorId,
            @RequestBody LezioneDto payload
    ) {
        try {
            requireAdmin(actorId);
            return ResponseEntity.ok(lezioneService.update(id, payload));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long actorId) {
        try {
            requireAdmin(actorId);
            lezioneService.delete(id);
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
