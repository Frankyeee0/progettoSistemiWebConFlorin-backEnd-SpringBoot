package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.dto.ClubDettaglioDto;
import com.florin.franco.UniHub_sistemiWeb.dto.ClubDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.service.ClubService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/club")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @PostMapping("/crea/{fondatoreId}")
    public ResponseEntity<?> creaClub(@RequestBody ClubDettaglioDto club, @PathVariable Long fondatoreId) {
        try {
            Club nuovoClub = clubService.creaClub(club, fondatoreId);
            return ResponseEntity.ok(nuovoClub);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllClubs")
    public ResponseEntity<List<ClubDto>> getAllClubs() {
        List<ClubDto> clubs = clubService.getTuttiClub();
        return ResponseEntity.ok(clubs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDettaglioDto> getClubById(@PathVariable Long id) {
        ClubDettaglioDto club = clubService.getClubDettaglio(id);
        return ResponseEntity.ok(club);
    }

    @PostMapping("/{clubId}/iscrivi/{utenteId}")
    public ResponseEntity<?> iscriviUtente(@PathVariable Long clubId, @PathVariable Long utenteId) {
        try {
            ClubDto clubAggiornato = clubService.iscriviUtente(clubId, utenteId);
            return ResponseEntity.ok(clubAggiornato);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{clubId}/disiscrivi/{utenteId}")
    public ResponseEntity<?> disiscriviUtente(@PathVariable Long clubId, @PathVariable Long utenteId) {
        try {
            Club clubAggiornato = clubService.disiscriviUtente(clubId, utenteId);
            return ResponseEntity.ok(clubAggiornato);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
