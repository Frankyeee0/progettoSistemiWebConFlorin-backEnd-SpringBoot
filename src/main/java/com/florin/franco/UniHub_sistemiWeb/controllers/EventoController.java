package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoCreateDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDettaglioDTO;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.florin.franco.UniHub_sistemiWeb.dto.EventoDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.service.EventoService;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @Autowired
    private AppUserRepository userRepository;

    @PostMapping("/crea")
    public ResponseEntity<?> creaEvento(@RequestBody EventoCreateDTO dto,
                                        @RequestParam String username) {
        try {
            AppUser creatore = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            return ResponseEntity.ok(eventoService.creaEvento(dto, creatore.getId()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Attenzione " +e.getMessage());
        }
    }

    @GetMapping
    public List<EventoDto> getAllEvents() {
        return eventoService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id, @RequestParam(required = false) String username) {
        try {
            return ResponseEntity.ok(eventoService.getEventDetails(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Attenzione " + e.getMessage());
        }
    }

    @PostMapping("/{eventoId}/iscrivi/{studenteId}")
    public ResponseEntity<?> iscriviStudente(@PathVariable Long eventoId, @PathVariable Long studenteId) {
        try {
            return ResponseEntity.ok(eventoService.iscriviStudente(eventoId, studenteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @PostMapping("/{eventoId}/disiscrivi/{studenteId}")
    public ResponseEntity<?> disiscriviStudente(@PathVariable Long eventoId, @PathVariable Long studenteId) {
        try {
            return ResponseEntity.ok(eventoService.disiscriviStudente(eventoId, studenteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }
    
    @GetMapping("/{eventoId}")
    public ResponseEntity<?> getEventoDettaglio(
            @PathVariable Long eventoId,
            @RequestParam(required = false) Long userId
    ) {
        try {
            EventoDettaglioDTO dettaglio = eventoService.getEventoDettaglio(eventoId, userId);
            return ResponseEntity.ok(dettaglio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Errore: " + e.getMessage());
        }
    }
}
