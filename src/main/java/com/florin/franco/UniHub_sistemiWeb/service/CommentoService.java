package com.florin.franco.UniHub_sistemiWeb.service;
 
import com.florin.franco.UniHub_sistemiWeb.dto.CommentoDto;
import com.florin.franco.UniHub_sistemiWeb.dto.UserLiteDto;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Club;
import com.florin.franco.UniHub_sistemiWeb.entity.Commento;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.ClubRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.CommentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EventoRepository;
 
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class CommentoService {
 
    @Autowired
    private CommentoRepository commentoRepository;
 
    @Autowired
    private AppUserRepository userRepository;
 
    @Autowired
    private EventoRepository eventoRepository;
 
    @Autowired
    private ClubRepository clubRepository;
 
    @Autowired
    private ModelMapper modelMapper;
 
    public List<CommentoDto> getAllCommenti() {
        return commentoRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
 
    public CommentoDto getCommentoById(Long id) {
        Commento commento = commentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento non trovato con ID " + id));
        return mapToDto(commento);
    }
 
    public List<CommentoDto> getCommentiByEvento(Long eventoId) {
        List<Commento> commenti = commentoRepository.findByEventoId(eventoId);
        return commenti.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
 
    public List<CommentoDto> getCommentiByClub(Long clubId) {
        List<Commento> commenti = commentoRepository.findByClubId(clubId);
        return commenti.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
 
    public CommentoDto creaCommento(CommentoDto dto) {
        Commento entity = new Commento();
 
        entity.setTesto(dto.getTesto());
        entity.setDataCreazione(LocalDateTime.now());
 

        if (dto.getAutore() == null || dto.getAutore().getId() == null) {
            throw new RuntimeException("Autore mancante nel commento");
        }
        AppUser autore = userRepository.findById(dto.getAutore().getId())
                .orElseThrow(() -> new RuntimeException("Autore non trovato"));
        entity.setAutore(autore);
 
        if (dto.getEventoId() != null) {
            Evento evento = eventoRepository.findById(dto.getEventoId())
                    .orElseThrow(() -> new RuntimeException("Evento non trovato"));
            entity.setEvento(evento);
        } else if (dto.getClubId() != null) {
            Club club = clubRepository.findById(dto.getClubId())
                    .orElseThrow(() -> new RuntimeException("Club non trovato"));
            entity.setClub(club);
        } else {
            throw new RuntimeException("Il commento deve essere associato a un evento o a un club");
        }
 
        Commento salvato = commentoRepository.save(entity);
        return mapToDto(salvato);
    }
 
    public CommentoDto aggiornaCommento(Long id, CommentoDto dto) {
        Commento commento = commentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commento non trovato con ID " + id));
 
        commento.setTesto(dto.getTesto());
        Commento aggiornato = commentoRepository.save(commento);
 
        return mapToDto(aggiornato);
    }
 
    public void eliminaCommento(Long id) {
        if (!commentoRepository.existsById(id)) {
            throw new RuntimeException("Commento non trovato con ID " + id);
        }
        commentoRepository.deleteById(id);
    }

 
    private CommentoDto mapToDto(Commento entity) {
        CommentoDto dto = modelMapper.map(entity, CommentoDto.class);
 
        if (entity.getAutore() != null) {
            dto.setAutore(new UserLiteDto(
                    entity.getAutore().getId(),
                    entity.getAutore().getUsername()
            ));
        }
 
        if (entity.getEvento() != null) {
            dto.setEventoId(entity.getEvento().getId());
        }
 
        if (entity.getClub() != null) {
            dto.setClubId(entity.getClub().getId());
        }
 
        return dto;
    }
    
    public List<CommentoDto> getCommentiByAutoreId(Long autoreId) {
        List<Commento> commenti = commentoRepository.findByAutoreId(autoreId);

        return commenti.stream()
                .map(c -> modelMapper.map(c, CommentoDto.class))
                .collect(Collectors.toList());
    	}
	}
 
 