package com.florin.franco.UniHub_sistemiWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.RegisterRequest;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.entity.Universita;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@Service
public class AuthService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private UniversitaRepository universitaRepository;

    @Autowired
    private DipartimentoRepository dipartimentoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AppUser registerUser(RegisterRequest request) {
    	
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username già esistente");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        Universita universita = universitaRepository.findById(request.getUniversitaId())
                .orElseThrow(() -> new RuntimeException("Università non trovata"));
        Dipartimento dipartimento = dipartimentoRepository.findById(request.getDipartimentoId())
                .orElseThrow(() -> new RuntimeException("Dipartimento non trovato"));

        if (!dipartimento.getUniversita().getId().equals(universita.getId())) {
            throw new RuntimeException("Il dipartimento selezionato non appartiene all’università scelta");
        }

        AppUser user = new AppUser();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setStudentId(request.getStudentId());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); 
        user.setEmail(request.getEmail());
        user.setRole(Ruolo.STUDENT);
        user.setDipartimento(dipartimento);

        return userRepository.save(user);
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    public boolean checkPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }
    
    public AppUser login(String username, String password) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password errata");
        }

        return user;
    }
}
