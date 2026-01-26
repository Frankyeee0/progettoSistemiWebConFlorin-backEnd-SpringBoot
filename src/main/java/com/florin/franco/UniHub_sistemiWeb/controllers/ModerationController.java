package com.florin.franco.UniHub_sistemiWeb.controllers;

import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.entity.Commento;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.repository.ClubRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.CommentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {

    @Autowired
    private CommentoRepository commentoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ClubRepository clubRepository;

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        if (!commentoRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Commento non trovato");
        }
        commentoRepository.deleteById(id);
        return ResponseEntity.ok("Commento eliminato");
    }

    @PostMapping("/events/{id}/hide")
    public ResponseEntity<?> hideEvent(@PathVariable Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        evento.setHidden(true);
        eventoRepository.save(evento);
        return ResponseEntity.ok("Evento nascosto");
    }

    @PostMapping("/events/{id}/restore")
    public ResponseEntity<?> restoreEvent(@PathVariable Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        evento.setHidden(false);
        eventoRepository.save(evento);
        return ResponseEntity.ok("Evento ripristinato");
    }

    @PostMapping("/clubs/{id}/suspend")
    public ResponseEntity<?> suspendClub(@PathVariable Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club non trovato"));
        club.setSuspended(true);
        clubRepository.save(club);
        return ResponseEntity.ok("Club sospeso");
    }

    @PostMapping("/clubs/{id}/restore")
    public ResponseEntity<?> restoreClub(@PathVariable Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club non trovato"));
        club.setSuspended(false);
        clubRepository.save(club);
        return ResponseEntity.ok("Club ripristinato");
    }
}
