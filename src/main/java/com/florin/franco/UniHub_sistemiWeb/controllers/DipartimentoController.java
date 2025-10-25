package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.entity.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.entity.Universita;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;
@RestController
@RequestMapping("/api/universita")
public class DipartimentoController {

    @Autowired
    private DipartimentoRepository dipartimentoRepository;

    @Autowired
    private UniversitaRepository universitaRepository;

    // 🔹 GET → restituisce tutti i dipartimenti di una università
    @GetMapping("/{universitaId}/dipartimenti")
    public ResponseEntity<List<Dipartimento>> getDipartimentiByUniversita(@PathVariable Long universitaId) {
        List<Dipartimento> dipartimenti = dipartimentoRepository.findByUniversitaId(universitaId);
        return ResponseEntity.ok(dipartimenti);
    }

    // 🔹 POST → aggiunge un nuovo dipartimento a una università
    @PostMapping("/{universitaId}/dipartimenti")
    public ResponseEntity<?> addDipartimento(
            @PathVariable Long universitaId,
            @RequestBody Dipartimento nuovoDipartimento
    ) {
        Universita universita = universitaRepository.findById(universitaId)
                .orElseThrow(() -> new RuntimeException("Università non trovata"));

        nuovoDipartimento.setUniversita(universita);
        Dipartimento salvato = dipartimentoRepository.save(nuovoDipartimento);

        return ResponseEntity.ok(salvato);
    }

    // 🔹 DELETE → elimina un dipartimento per ID
    @DeleteMapping("/dipartimenti/{dipartimentoId}")
    public ResponseEntity<String> deleteDipartimento(@PathVariable Long dipartimentoId) {
        Dipartimento dip = dipartimentoRepository.findById(dipartimentoId)
                .orElseThrow(() -> new RuntimeException("Dipartimento non trovato"));

        dipartimentoRepository.delete(dip);
        return ResponseEntity.ok("Dipartimento eliminato con successo");
    }
}