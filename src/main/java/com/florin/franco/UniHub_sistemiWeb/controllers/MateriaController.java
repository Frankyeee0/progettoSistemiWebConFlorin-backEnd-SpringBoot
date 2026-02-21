package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.dto.MateriaDto;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.service.MateriaService;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@RestController
@RequestMapping("/api/materie")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private AppUserRepository userRepository;

    @GetMapping
    public List<MateriaDto> list() {
        return materiaService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long actorId, @RequestBody MateriaDto payload) {
        try {
            requireAdmin(actorId);
            return ResponseEntity.ok(materiaService.create(payload));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam Long actorId,
            @RequestBody MateriaDto payload
    ) {
        try {
            requireAdmin(actorId);
            return ResponseEntity.ok(materiaService.update(id, payload));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long actorId) {
        try {
            requireAdmin(actorId);
            materiaService.delete(id);
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
