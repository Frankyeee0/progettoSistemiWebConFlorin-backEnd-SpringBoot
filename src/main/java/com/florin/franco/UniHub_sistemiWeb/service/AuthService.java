package com.florin.franco.UniHub_sistemiWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public AppUser registerUser(String username, String rawPassword, Ruolo  role, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("❌ Username già esistente");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("❌ Email già registrata");
        }

        String encoded = passwordEncoder.encode(rawPassword);
        return userRepository.save(new AppUser(null, username, encoded, role, email));
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    public boolean checkPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }
}