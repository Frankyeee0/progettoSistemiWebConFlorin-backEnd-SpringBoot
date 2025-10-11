package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ClubRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private AppUserRepository userRepository;

    // 🔹 Creazione club (solo Admin/SuperAdmin)
    public Club creaClub(Club club, Long fondatoreId) {
        AppUser fondatore = userRepository.findById(fondatoreId)
                .orElseThrow(() -> new RuntimeException("Fondatore non trovato"));

        if (fondatore.getRole() != Ruolo.ADMIN && fondatore.getRole() != Ruolo.SUPERADMIN) {
            throw new RuntimeException("Solo gli admin possono creare club!");
        }

        club.setFondatore(fondatore);
        return clubRepository.save(club);
    }

    // 🔹 Lista di tutti i club
    public List<Club> getTuttiClub() {
        return clubRepository.findAll();
    }

    // 🔹 Iscrizione utente
    public Club iscriviUtente(Long clubId, Long utenteId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club non trovato"));
        AppUser utente = userRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (club.getPostiDisponibili() <= 0) {
            throw new RuntimeException("Club pieno!");
        }

        if (club.getMembri().contains(utente)) {
            throw new RuntimeException("Utente già iscritto!");
        }

        club.getMembri().add(utente);
        return clubRepository.save(club);
    }

    // 🔹 Disiscrizione
    public Club disiscriviUtente(Long clubId, Long utenteId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club non trovato"));
        AppUser utente = userRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        club.getMembri().remove(utente);
        return clubRepository.save(club);
    }
    
    public Club getClubDettaglio(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club non trovato"));
        club.getCommenti().size();
        club.getFeedbacks().size();
        return club;
    }

}