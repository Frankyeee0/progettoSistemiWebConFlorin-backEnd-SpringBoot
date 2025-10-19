package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.api.dto.ClubDettaglioDTO;
import com.florin.franco.UniHub_sistemiWeb.api.projection.ClubSummary;
import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.repository.ClubRepository;
import com.florin.franco.UniHub_sistemiWeb.service.ClubService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/club")
@RequiredArgsConstructor
public class ClubController {
	
	@Autowired
    private ClubService clubService;
	@Autowired
    private ClubRepository repo;

    // ✅ Creazione club
    @PostMapping("/crea/{fondatoreId}")
    public ResponseEntity<?> creaClub(@RequestBody Club club, @PathVariable Long fondatoreId) {
        try {
            return ResponseEntity.ok(clubService.creaClub(club, fondatoreId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    // ✅ Lista “leggera”: id, nome, descrizione (projection)
    @GetMapping
    public ResponseEntity<List<ClubSummary>> list() {
        return ResponseEntity.ok(repo.findAllProjectedBy());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDettaglioDTO> getClub(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubDettaglio(id));
    }

    @PostMapping("/{clubId}/iscrivi/{utenteId}")
    public ResponseEntity<?> iscriviUtente(@PathVariable Long clubId, @PathVariable Long utenteId) {
        try {
            return ResponseEntity.ok(clubService.iscriviUtente(clubId, utenteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @PostMapping("/{clubId}/disiscrivi/{utenteId}")
    public ResponseEntity<?> disiscriviUtente(@PathVariable Long clubId, @PathVariable Long utenteId) {
        try {
            return ResponseEntity.ok(clubService.disiscriviUtente(clubId, utenteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }
}
