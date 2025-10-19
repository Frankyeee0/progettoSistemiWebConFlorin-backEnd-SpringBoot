package com.florin.franco.UniHub_sistemiWeb.controllers;


import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.api.dto.UniversitaDTO;
import com.florin.franco.UniHub_sistemiWeb.api.mapper.UniversitaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;


@RestController
@RequestMapping("/api/universita")
public class UniversitaController {

    @Autowired
    private UniversitaRepository universitaRepository;

    // ✅ 1. Ottieni tutte le università
    @GetMapping
    public ResponseEntity<List<UniversitaDTO>> getAllUniversita() {
        var list = universitaRepository.findAll();
        var dto  = list.stream().map(UniversitaMapper::toDto).toList();
        return ResponseEntity.ok(dto);
    }
}
