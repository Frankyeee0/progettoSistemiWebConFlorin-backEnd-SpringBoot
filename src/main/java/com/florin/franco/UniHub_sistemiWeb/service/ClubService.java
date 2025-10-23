package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.ClubDettaglioDto;
import com.florin.franco.UniHub_sistemiWeb.dto.ClubDto;
import com.florin.franco.UniHub_sistemiWeb.dto.UserLiteDto;
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

	    @Autowired
	    private ModelMapper modelMapper;

	    public Club creaClub(Club club, Long fondatoreId) {
	        AppUser fondatore = userRepository.findById(fondatoreId)
	                .orElseThrow(() -> new RuntimeException("Fondatore non trovato"));

	        if (fondatore.getRole() != Ruolo.ADMIN && fondatore.getRole() != Ruolo.SUPERADMIN) {
	            throw new RuntimeException("Solo gli admin possono creare club!");
	        }

	        club.setFondatore(fondatore);
	        return clubRepository.save(club);
	    }

	    public List<ClubDto> getTuttiClub() {
	    	
	        List<Club> listaClubEntity = clubRepository.findAll();
	        List<ClubDto> listaClubDto = new ArrayList<>();

	        listaClubEntity.forEach(elem -> {
	            listaClubDto.add(modelMapper.map(elem, ClubDto.class));
	        });

	        return listaClubDto;
	    }

	    public ClubDto iscriviUtente(Long clubId, Long utenteId) {
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
	        Club clubAggiornato = clubRepository.save(club);

	        ClubDto dto = modelMapper.map(clubAggiornato, ClubDto.class);
	        return dto;
	    }

	    public Club disiscriviUtente(Long clubId, Long utenteId) {
	        Club club = clubRepository.findById(clubId)
	                .orElseThrow(() -> new RuntimeException("Club non trovato"));
	        AppUser utente = userRepository.findById(utenteId)
	                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

	        club.getMembri().remove(utente);
	        return clubRepository.save(club);
	    }

	    public ClubDettaglioDto getClubDettaglio(Long id) {
	        Club club = clubRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Club non trovato"));

	        modelMapper.typeMap(Club.class, ClubDettaglioDto.class)
	                .addMappings(m -> m.skip(ClubDettaglioDto::setMembri));

	        ClubDettaglioDto dto = modelMapper.map(club, ClubDettaglioDto.class);

	        List<UserLiteDto> membriDto = club.getMembri().stream()
	                .map(u -> new UserLiteDto(u.getId(), u.getUsername()))
	                .toList();

	        dto.setMembri(membriDto);

	        dto.setFondatoreUsername(club.getFondatore() != null ? club.getFondatore().getUsername() : null);

	        return dto;
	    }
	}