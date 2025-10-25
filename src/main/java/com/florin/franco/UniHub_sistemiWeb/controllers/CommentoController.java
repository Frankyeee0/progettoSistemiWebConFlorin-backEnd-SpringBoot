package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.florin.franco.UniHub_sistemiWeb.dto.CommentoDto;
import com.florin.franco.UniHub_sistemiWeb.service.CommentoService;
 
@RestController
@RequestMapping("/api/commenti")
public class CommentoController {
 
    @Autowired
    private CommentoService commentoService;
 
    @GetMapping
    public ResponseEntity<List<CommentoDto>> getAllCommenti() {
        return ResponseEntity.ok(commentoService.getAllCommenti());
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<CommentoDto> getCommentoById(@PathVariable Long id) {
        return ResponseEntity.ok(commentoService.getCommentoById(id));
    }
 
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<CommentoDto>> getCommentiByEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(commentoService.getCommentiByEvento(eventoId));
    }
 
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<CommentoDto>> getCommentiByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(commentoService.getCommentiByClub(clubId));
    }
 
    @PostMapping
    public ResponseEntity<CommentoDto> creaCommento(@RequestBody CommentoDto commentoDto) {
        CommentoDto nuovoCommento = commentoService.creaCommento(commentoDto);
        return ResponseEntity.ok(nuovoCommento);
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<CommentoDto> aggiornaCommento(@PathVariable Long id, @RequestBody CommentoDto commentoDto) {
        CommentoDto aggiornato = commentoService.aggiornaCommento(id, commentoDto);
        return ResponseEntity.ok(aggiornato);
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminaCommento(@PathVariable Long id) {
        commentoService.eliminaCommento(id);
        return ResponseEntity.ok("Commento con ID " + id + " eliminato con successo");
    }
}