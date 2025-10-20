package com.florin.franco.UniHub_sistemiWeb.controllers;


import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.dto.UniversitaDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Universita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;
import com.florin.franco.UniHub_sistemiWeb.service.UniversitaService;


@RestController
@RequestMapping("/api/universita")
public class UniversitaController {

		@Autowired
		private  UniversitaService universitaService;
		
	  @GetMapping
	    public ResponseEntity<List<UniversitaDto>> getAllUniversita() {
	        return ResponseEntity.ok(universitaService.getAllUniversita());
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<UniversitaDto> getUniversita(@PathVariable Long id) {
	        return ResponseEntity.ok(universitaService.getUniversitaById(id));
	    }
	}
