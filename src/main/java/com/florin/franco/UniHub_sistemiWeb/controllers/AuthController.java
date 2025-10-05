package com.florin.franco.UniHub_sistemiWeb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.dto.RegisterRequest;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController  {
	
	 	@Autowired
	    private AuthService authService;

	    @PostMapping("/register")
	    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
	        try {
	            AppUser user = authService.registerUser(
	                    request.getUsername(),
	                    request.getPassword(),
	                    request.getRole()
	            );

	            return ResponseEntity
	                    .status(HttpStatus.CREATED)
	                    .body("✅ Utente registrato con successo: " + user.getUsername());
	        } catch (RuntimeException e) {
	            return ResponseEntity
	                    .status(HttpStatus.BAD_REQUEST)
	                    .body(e.getMessage());
	        }
	    }
	    
	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
	        try {
	            // Cerca l’utente nel DB
	            AppUser user = authService.findByUsername(request.getUsername());

	            // Verifica la password
	            if (!authService.checkPassword(request.getPassword(), user.getPassword())) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Password errata");
	            }

	            return ResponseEntity.ok("✅ Login effettuato con successo da " + user.getUsername());

	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	        }
	    }
	}