package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

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

    @PostMapping("/crea/{creatoreId}")
    public ResponseEntity<?> creaEvento(@RequestBody Evento evento, @PathVariable Long creatoreId) {
        try {
            return ResponseEntity.ok(eventoService.creaEvento(evento, creatoreId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Attenzione " +e.getMessage());
        }
    }

    @GetMapping
    public List<EventoDto> getAllEvents() {
        return eventoService.getAllEvents();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable Long id) {
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
}
