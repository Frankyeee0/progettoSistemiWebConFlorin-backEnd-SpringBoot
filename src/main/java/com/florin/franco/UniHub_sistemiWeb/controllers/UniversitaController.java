package com.florin.franco.UniHub_sistemiWeb.controllers;


import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.dto.UniversitaDto;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.florin.franco.UniHub_sistemiWeb.service.UniversitaService;


@RestController
@RequestMapping("/api/universita")
public class UniversitaController {

		@Autowired
		private  UniversitaService universitaService;
        @Autowired
        private AppUserRepository userRepository;
		
	  @GetMapping
	    public ResponseEntity<List<UniversitaDto>> getAllUniversita() {
	        return ResponseEntity.ok(universitaService.getAllUniversita());
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<UniversitaDto> getUniversita(@PathVariable Long id) {
	        return ResponseEntity.ok(universitaService.getUniversitaById(id));
	    }

        @PostMapping
        public ResponseEntity<?> create(@RequestParam Long actorId, @RequestBody UniversitaDto payload) {
            try {
                requireAdmin(actorId);
                return ResponseEntity.ok(universitaService.createUniversita(payload.getNome()));
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam Long actorId) {
            try {
                requireAdmin(actorId);
                universitaService.deleteUniversita(id);
                return ResponseEntity.ok("Universita eliminata");
            } catch (RuntimeException e) {
                if (e instanceof ResponseStatusException) {
                    throw e;
                }
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        private void requireAdmin(Long actorId) {
            if (actorId == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato");
            }
            AppUser user = userRepository.findById(actorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato"));
            if (user.getRole() != Ruolo.ADMIN) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permesso negato");
            }
        }
	}
