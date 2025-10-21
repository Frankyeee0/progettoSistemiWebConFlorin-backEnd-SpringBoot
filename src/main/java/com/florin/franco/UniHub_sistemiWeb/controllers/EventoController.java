package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.service.EventoService;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // ✅ Creazione evento (solo Admin o SuperAdmin)
    @PostMapping("/crea/{creatoreId}")
    public ResponseEntity<?> creaEvento(@RequestBody Evento evento, @PathVariable Long creatoreId) {
        try {
            return ResponseEntity.ok(eventoService.creaEvento(evento, creatoreId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    // ✅ Lista eventi
    @GetMapping
    public List<EventoDTO> getEventi() {
        return eventoService.getAllEventi();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDetailDTO> getEvento(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.getEventoDettaglio(id));
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
