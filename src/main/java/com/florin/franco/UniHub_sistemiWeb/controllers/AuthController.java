package com.florin.franco.UniHub_sistemiWeb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.florin.franco.UniHub_sistemiWeb.dto.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.dto.LoginRequest;
import com.florin.franco.UniHub_sistemiWeb.dto.LoginResponse;
import com.florin.franco.UniHub_sistemiWeb.dto.RegisterRequest;
import com.florin.franco.UniHub_sistemiWeb.dto.Universita;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;
import com.florin.franco.UniHub_sistemiWeb.service.AuthService;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UniversitaRepository universitaRepository;

    @Autowired
    private DipartimentoRepository dipartimentoRepository;
    
    @Autowired
    private AuthService authService;
    // 🔹 Endpoint di registrazione
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AppUser user = authService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("✅ Utente registrato con successo: " + user.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ Errore: " + e.getMessage());
        }
    }

	 	
	 	@GetMapping("/check-username")
	 	public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
	 	    boolean exists = appUserRepository.existsByUsername(username);
	 	    return ResponseEntity.ok(exists);
	 	}

	 	@GetMapping("/check-email")
	 	public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
	 	    boolean exists = appUserRepository.existsByEmail(email);
	 	    return ResponseEntity.ok(exists);
	 	}


	    
	 	@PostMapping("/login")
	 	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
	 	    try {
	 	        AppUser user = authService.login(request.getUsername(), request.getPassword());

	 	        // 🔹 Costruisci il DTO di risposta
	 	        LoginResponse response = new LoginResponse(
	 	            "✅ Login effettuato con successo",
                     user.getId(),
	 	            user.getUsername(),
	 	            user.getRole().name()
	 	            );
	 	        

	 	        return ResponseEntity.ok(response);

	 	    } catch (RuntimeException e) {
	 	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	 	                .body("❌ Errore durante il login: " + e.getMessage());
	 	    }
	 	}


	}