package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.CategoriaEvento;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.service.CategoriaEventoService;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@RestController
@RequestMapping("/api/categories")
public class CategoriaEventoController {

    @Autowired
    private CategoriaEventoService categoriaEventoService;
    @Autowired
    private AppUserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<CategoriaEvento>> list() {
        return ResponseEntity.ok(categoriaEventoService.listAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long actorId, @RequestBody CategoriaEvento payload) {
        try {
            requireAdmin(actorId);
            return ResponseEntity.ok(categoriaEventoService.create(payload.getNome()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long actorId) {
        try {
            requireAdmin(actorId);
            categoriaEventoService.delete(id);
            return ResponseEntity.ok("Categoria eliminata");
        } catch (RuntimeException e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void requireAdmin(Long actorId) {
        if (actorId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato");
        }
        AppUser user = userRepository.findById(actorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato"));
        if (user.getRole() != Ruolo.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato");
        }
    }
}
