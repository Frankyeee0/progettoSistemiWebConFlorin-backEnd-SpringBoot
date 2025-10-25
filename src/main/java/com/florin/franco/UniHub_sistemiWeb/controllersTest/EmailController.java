package com.florin.franco.UniHub_sistemiWeb.controllersTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.florin.franco.UniHub_sistemiWeb.service.EmailService;

@Profile("test")
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
	        return ResponseEntity.badRequest().body(e.getMessage());
	     }
	}
}

