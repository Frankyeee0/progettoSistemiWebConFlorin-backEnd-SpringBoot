package com.florin.franco.UniHub_sistemiWeb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.entity.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;

@RestController
@RequestMapping("/api/universita")
public class DipartimentoController {

    @Autowired
    private DipartimentoRepository dipartimentoRepository;

    // ✅ 2. Ottieni i dipartimenti di una specifica università
    @GetMapping("/{universitaId}/dipartimenti")
    public ResponseEntity<List<Dipartimento>> getDipartimentiByUniversita(@PathVariable Long universitaId) {
        List<Dipartimento> dipartimenti = dipartimentoRepository.findByUniversitaId(universitaId);
        return ResponseEntity.ok(dipartimenti);
    }
}