package com.florin.franco.UniHub_sistemiWeb.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.dto.Universita;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;


@RestController
@RequestMapping("/api/universita")
public class UniversitaController {

    @Autowired
    private UniversitaRepository universitaRepository;

    // ✅ 1. Ottieni tutte le università
    @GetMapping
    public ResponseEntity<List<Universita>> getAllUniversita() {
        List<Universita> universitaList = universitaRepository.findAll();
        return ResponseEntity.ok(universitaList);
    }
}
