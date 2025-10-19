package com.florin.franco.UniHub_sistemiWeb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.dto.RegisterRequest;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {
	@Autowired
	private EmailService emailService;
	
	@GetMapping
	public ResponseEntity<?> register(@RequestBody String email, String username) {
		try {
	        emailService.sendWelcomeEmail(email, username);
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body("email mandata con successo " +username);
	    } catch (RuntimeException e) {
	        return ResponseEntity.badRequest().body("❌ Errore: " + e.getMessage());
	    }
}
}

