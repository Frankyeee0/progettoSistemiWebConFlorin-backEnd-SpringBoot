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

import com.florin.franco.UniHub_sistemiWeb.dto.DipartimentoDto;
import com.florin.franco.UniHub_sistemiWeb.service.DipartimentoService;
@RestController
@RequestMapping("/api/universita")
public class DipartimentoController {

    @Autowired
    private DipartimentoService dipartimentoService;

    @GetMapping("/{universitaId}/dipartimenti")
    public ResponseEntity<List<DipartimentoDto>> getDipartimenti(@PathVariable Long universitaId) {
        return ResponseEntity.ok(dipartimentoService.getDipartimentiByUniversita(universitaId));
    }

    @PostMapping("/{universitaId}/dipartimenti")
    public ResponseEntity<DipartimentoDto> addDipartimento(
            @PathVariable Long universitaId,
            @RequestBody DipartimentoDto dto
    ) {
        return ResponseEntity.ok(dipartimentoService.createDipartimento(universitaId, dto));
    }

    @PutMapping("/dipartimenti/{dipartimentoId}")
    public ResponseEntity<DipartimentoDto> updateDipartimento(
            @PathVariable Long dipartimentoId,
            @RequestBody DipartimentoDto dto
    ) {
        return ResponseEntity.ok(dipartimentoService.updateDipartimento(dipartimentoId, dto));
    }

    @DeleteMapping("/dipartimenti/{dipartimentoId}")
    public ResponseEntity<String> deleteDipartimento(@PathVariable Long dipartimentoId) {
        dipartimentoService.deleteDipartimento(dipartimentoId);
        return ResponseEntity.ok("Dipartimento eliminato con successo");
    }
}