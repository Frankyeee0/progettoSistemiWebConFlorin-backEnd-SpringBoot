package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoCreateDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDTO;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import com.florin.franco.UniHub_sistemiWeb.dto.EventoDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
=======
>>>>>>> release
import com.florin.franco.UniHub_sistemiWeb.service.EventoService;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @PostMapping("/crea/{creatoreId}")
    public ResponseEntity<?> creaEvento(@RequestBody Evento evento, @PathVariable Long creatoreId) {
    @Autowired
    private AppUserRepository userRepository;

    // ✅ Creazione evento (solo Admin o SuperAdmin)
    @PostMapping("/crea")
    public ResponseEntity<?> creaEvento(@RequestBody EventoCreateDTO dto,
                                        @RequestParam String username) {
        try {
            AppUser creatore = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            // 🔹 Ora il service si occupa di tutto
            return ResponseEntity.ok(eventoService.creaEvento(dto, creatore.getId()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Attenzione " +e.getMessage());
        }
    }

    @GetMapping
<<<<<<< HEAD
    public List<EventoDto> getAllEvents() {
        return eventoService.getAllEvents();
=======
    public List<EventoDTO> EventoDTO() {
        return eventoService.getAllEventi();
>>>>>>> release
    }

    @GetMapping("/{id}")
<<<<<<< HEAD
    public ResponseEntity<?> getEvent(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(eventoService.getEventDetails(id));
=======
    public ResponseEntity<?> getEvento(
            @PathVariable Long id,
            @RequestParam(required = false) String username
    ) {
        try {
            return ResponseEntity.ok(eventoService.getEventoDettaglio(id, username));
>>>>>>> release
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
