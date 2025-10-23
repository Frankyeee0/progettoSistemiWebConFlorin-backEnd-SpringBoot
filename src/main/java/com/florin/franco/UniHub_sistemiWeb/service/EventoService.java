package com.florin.franco.UniHub_sistemiWeb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.EventoDto;
import com.florin.franco.UniHub_sistemiWeb.dto.UserLiteDto;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.repository.AppUserRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.EventoRepository;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private AppUserRepository userRepository;
    
    @Autowired
    ModelMapper modelMapper;
   
    public Evento creaEvento(Evento evento, Long creatoreId) {
        AppUser creatore = userRepository.findById(creatoreId)
                .orElseThrow(() -> new RuntimeException("Creatore non trovato"));

        if (creatore.getRole() != Ruolo.ADMIN && creatore.getRole() != Ruolo.SUPERADMIN) {
            throw new RuntimeException("Solo gli admin possono creare eventi!");
        }

        evento.setCreatore(creatore);
        return eventoRepository.save(evento);
    }

    public List<EventoDto> getAllEvents() {
    	List<Evento> listEventsEntity= eventoRepository.findAll();
    	List<EventoDto> listEventsDto = new ArrayList<EventoDto>();
    	
    	listEventsEntity.forEach(elem ->{
    		listEventsDto.add(modelMapper.map(elem,EventoDto.class));
    	});
        return listEventsDto;
    }

    public EventoDto iscriviStudente(Long eventoId, Long studenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        AppUser studente = userRepository.findById(studenteId)
                .orElseThrow(() -> new RuntimeException("Studente non trovato"));

        if (studente.getRole() != Ruolo.STUDENT) {
            throw new RuntimeException("Solo gli studenti possono iscriversi agli eventi!");
        }

        if (evento.getDeadlineIscrizione() != null &&
            LocalDateTime.now().isAfter(evento.getDeadlineIscrizione())) {
            throw new RuntimeException("Le iscrizioni sono chiuse per questo evento!");
        }

        if (evento.getPostiDisponibili() <= 0) {
            throw new RuntimeException("Posti esauriti!");
        }

        if (evento.getIscritti().contains(studente)) {
            throw new RuntimeException("Studente già iscritto!");
        }

        evento.getIscritti().add(studente);
        Evento eventoAggiornato = eventoRepository.save(evento);

        EventoDto dto = modelMapper.map(eventoAggiornato, EventoDto.class);

        modelMapper.typeMap(Evento.class, EventoDto.class)
                .addMappings(m -> m.skip(EventoDto::setIscritti));

        List<UserLiteDto> iscrittiDto = eventoAggiornato.getIscritti().stream()
                .map(u -> new UserLiteDto(u.getId(), u.getUsername()))
                .toList();

        dto.setIscritti(iscrittiDto);

        return dto;
    }

    public Evento disiscriviStudente(Long eventoId, Long studenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        AppUser studente = userRepository.findById(studenteId)
                .orElseThrow(() -> new RuntimeException("Studente non trovato"));

        evento.getIscritti().remove(studente);
        return eventoRepository.save(evento);
    }
    
    public EventoDto getEventDetails(Long id) {
    	
        Evento event = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        EventoDto eventdtoDto= modelMapper.map(event,EventoDto.class);

        return eventdtoDto;
    }

}