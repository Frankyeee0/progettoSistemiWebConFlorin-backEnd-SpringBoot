package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.service.EventoService;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {
    private EventoService eventoService;

    private AppUserRepository userRepository;

    // ✅ Creazione evento (solo Admin o SuperAdmin)
    @PostMapping("/crea")
    public ResponseEntity<?> creaEvento(@RequestBody Evento evento, @RequestParam String username) {
        try {
            AppUser creatore = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            return ResponseEntity.ok(eventoService.creaEvento(evento, creatore.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    // ✅ Lista eventi
    @GetMapping
    public List<Evento> getEventi() {
        return eventoService.getTuttiEventi();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvento(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventoService.getEventoDettaglio(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    // ✅ Iscrizione studente
    @PostMapping("/{eventoId}/iscrivi/{studenteId}")
    public ResponseEntity<?> iscriviStudente(@PathVariable Long eventoId, @PathVariable Long studenteId) {
        try {
            return ResponseEntity.ok(eventoService.iscriviStudente(eventoId, studenteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    // ✅ Disiscrizione studente
    @PostMapping("/{eventoId}/disiscrivi/{studenteId}")
    public ResponseEntity<?> disiscriviStudente(@PathVariable Long eventoId, @PathVariable Long studenteId) {
        try {
            return ResponseEntity.ok(eventoService.disiscriviStudente(eventoId, studenteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }
}
