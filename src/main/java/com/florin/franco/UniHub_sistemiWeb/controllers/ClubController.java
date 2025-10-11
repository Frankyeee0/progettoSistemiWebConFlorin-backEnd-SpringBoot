package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.service.ClubService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/club")
public class ClubController {

    @Autowired
    private ClubService clubService;

    // ✅ Creazione club (solo Admin o SuperAdmin)
    @PostMapping("/crea/{fondatoreId}")
    public ResponseEntity<?> creaClub(@RequestBody Club club, @PathVariable Long fondatoreId) {
        try {
            return ResponseEntity.ok(clubService.creaClub(club, fondatoreId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    // ✅ Lista club
    @GetMapping
    public List<Club> getClub() {
        return clubService.getTuttiClub();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClub(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clubService.getClubDettaglio(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }


    // ✅ Iscrizione utente
    @PostMapping("/{clubId}/iscrivi/{utenteId}")
    public ResponseEntity<?> iscriviUtente(@PathVariable Long clubId, @PathVariable Long utenteId) {
        try {
            return ResponseEntity.ok(clubService.iscriviUtente(clubId, utenteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    // ✅ Disiscrizione utente
    @PostMapping("/{clubId}/disiscrivi/{utenteId}")
    public ResponseEntity<?> disiscriviUtente(@PathVariable Long clubId, @PathVariable Long utenteId) {
        try {
            return ResponseEntity.ok(clubService.disiscriviUtente(clubId, utenteId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }
}