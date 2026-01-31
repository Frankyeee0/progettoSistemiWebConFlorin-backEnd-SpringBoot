package com.florin.franco.UniHub_sistemiWeb.controllers;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.entity.Commento;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.entity.ModerationAction;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ClubRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.CommentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EventoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ModerationActionRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {

    @Autowired
    private CommentoRepository commentoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private ModerationActionRepository moderationActionRepository;

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @RequestParam Long actorId) {
        AppUser actor = requireSuperAdmin(actorId);
        if (!commentoRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Commento non trovato");
        }
        commentoRepository.deleteById(id);
        saveAction(actor, "DELETE_COMMENT", "COMMENT", id);
        return ResponseEntity.ok("Commento eliminato");
    }

    @PostMapping("/events/{id}/hide")
    public ResponseEntity<?> hideEvent(@PathVariable Long id) {
        AppUser actor = requireSuperAdmin(1L);
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        evento.setHidden(true);
        eventoRepository.save(evento);
        saveAction(actor, "HIDE_EVENT", "EVENT", id);
        return ResponseEntity.ok("Evento nascosto");
    }

    @PostMapping("/events/{id}/restore")
    public ResponseEntity<?> restoreEvent(@PathVariable Long id, @RequestParam Long actorId) {
        AppUser actor = requireSuperAdmin(actorId);
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        evento.setHidden(false);
        eventoRepository.save(evento);
        saveAction(actor, "RESTORE_EVENT", "EVENT", id);
        return ResponseEntity.ok("Evento ripristinato");
    }

    @PostMapping("/clubs/{id}/suspend")
    public ResponseEntity<?> suspendClub(@PathVariable Long id, @RequestParam Long actorId) {
        AppUser actor = requireSuperAdmin(actorId);
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club non trovato"));
        club.setSuspended(true);
        clubRepository.save(club);
        saveAction(actor, "SUSPEND_CLUB", "CLUB", id);
        return ResponseEntity.ok("Club sospeso");
    }

    @PostMapping("/clubs/{id}/restore")
    public ResponseEntity<?> restoreClub(@PathVariable Long id, @RequestParam Long actorId) {
        AppUser actor = requireSuperAdmin(actorId);
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club non trovato"));
        club.setSuspended(false);
        clubRepository.save(club);
        saveAction(actor, "RESTORE_CLUB", "CLUB", id);
        return ResponseEntity.ok("Club ripristinato");
    }

    private AppUser requireSuperAdmin(Long actorId) {
        if (actorId == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato");
        }
        AppUser user = userRepository.findById(actorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato"));
        if (user.getRole() != Ruolo.SUPERADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato");
        }
        return user;
    }

    private void saveAction(AppUser actor, String action, String targetType, Long targetId) {
        ModerationAction log = new ModerationAction();
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setActorId(actor.getId());
        log.setActorUsername(actor.getUsername());
        moderationActionRepository.save(log);
    }
}
