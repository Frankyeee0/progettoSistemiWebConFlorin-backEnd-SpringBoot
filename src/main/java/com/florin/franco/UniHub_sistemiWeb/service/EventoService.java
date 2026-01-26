package com.florin.franco.UniHub_sistemiWeb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import com.florin.franco.UniHub_sistemiWeb.api.dto.CreatoreDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoCreateDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDettaglioDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoUpdateDTO;
import com.florin.franco.UniHub_sistemiWeb.api.mapper.EventoMapper;
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

    @Autowired
    private EmailService emailService;
   

    public EventoDettaglioDTO creaEvento(EventoCreateDTO dto, Long creatoreId) {
        AppUser creatore = userRepository.findById(creatoreId)
                .orElseThrow(() -> new RuntimeException("Creatore non trovato"));

        if (creatore.getRole() != Ruolo.ADMIN && creatore.getRole() != Ruolo.SUPERADMIN) {
            throw new RuntimeException("Solo gli admin possono creare eventi!");
        }
        Evento evento = EventoMapper.fromCreateDTO(dto);
        evento.setCreatore(creatore);

        Evento salvato = eventoRepository.save(evento);
        notifyFollowersNewEvent(salvato);
        return EventoMapper.toDTO(salvato);
    }

    public List<EventoDto> getAllEvents() {
    	List<Evento> listEventsEntity= eventoRepository.findAll()
                .stream()
                .filter(e -> !e.isHidden())
                .toList();
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
                .map(u -> new UserLiteDto(u.getId(), u.getUsername(), u.getProfileImage()))
                .toList();

        dto.setIscritti(iscrittiDto);

        notifySignup(evento, studente);
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
        eventdtoDto.setPostiDisponibili(event.getPostiDisponibili());
        eventdtoDto.setDataFine(event.getDataFine());

        List<UserLiteDto> iscrittiDto = event.getIscritti().stream()
                .map(u -> new UserLiteDto(u.getId(), u.getUsername(), u.getProfileImage()))
                .toList();
        eventdtoDto.setIscritti(iscrittiDto);

        return eventdtoDto;}

    public EventoDettaglioDTO aggiornaEvento(Long eventoId, EventoUpdateDTO dto, Long editorId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        if (editorId != null) {
            AppUser editor = userRepository.findById(editorId)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));
            if (editor.getRole() != Ruolo.ADMIN && editor.getRole() != Ruolo.SUPERADMIN) {
                throw new RuntimeException("Solo gli admin possono modificare eventi!");
            }
        }

        String oldWhen = formatDate(evento.getDataInizio());
        String oldWhere = evento.getLuogo();

        if (dto.getTitolo() != null) {
            evento.setTitolo(dto.getTitolo());
        }
        if (dto.getDescrizione() != null) {
            evento.setDescrizione(dto.getDescrizione());
        }
        if (dto.getLuogo() != null) {
            evento.setLuogo(dto.getLuogo());
        }
        if (dto.getDataInizio() != null) {
            evento.setDataInizio(dto.getDataInizio());
        }
        if (dto.getDataFine() != null) {
            evento.setDataFine(dto.getDataFine());
        }
        if (dto.getPostiTotali() != null) {
            evento.setPostiTotali(dto.getPostiTotali());
        }
        if (dto.getDeadlineIscrizione() != null) {
            evento.setDeadlineIscrizione(dto.getDeadlineIscrizione());
        }

        Evento salvato = eventoRepository.save(evento);

        boolean changed = !equalsNullable(oldWhere, salvato.getLuogo())
                || !equalsNullable(oldWhen, formatDate(salvato.getDataInizio()));
        if (changed) {
            notifyEventUpdate(salvato);
        }

        return EventoMapper.toDTO(salvato);
    }

    public EventoDettaglioDTO getEventoDettaglio(Long eventoId, Long userId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        boolean userIscritto = false;
        if (userId != null) {
            userIscritto = evento.getIscritti().stream()
                    .anyMatch(u -> u.getId().equals(userId));
        }

        return toDettaglioDTO(evento, userIscritto);
    }

    private EventoDettaglioDTO toDettaglioDTO(Evento evento, boolean userIscritto) {
        EventoDettaglioDTO dto = new EventoDettaglioDTO();
        dto.setId(evento.getId());
        dto.setTitolo(evento.getTitolo());
        dto.setDescrizione(evento.getDescrizione());
        dto.setDataInizio(evento.getDataInizio());
        dto.setDataFine(evento.getDataFine());
        dto.setLuogo(evento.getLuogo());
        dto.setPostiTotali(evento.getPostiTotali());
        dto.setPostiDisponibili(evento.getPostiDisponibili());
        dto.setDeadlineIscrizione(evento.getDeadlineIscrizione());
        dto.setUserIscritto(userIscritto);

        var creatore = evento.getCreatore();
        if (creatore != null) {
            dto.setCreatore(new CreatoreDTO(
                    creatore.getId(),
                    creatore.getUsername()
                  
            ));
        }

        return dto;
    }

    private void notifyFollowersNewEvent(Evento evento) {
        if (evento.getCreatore() == null) return;
        var followers = userRepository.findFollowerByUserId(evento.getCreatore().getId());
        String when = formatDate(evento.getDataInizio());
        String where = evento.getLuogo();
        for (AppUser follower : followers) {
            if (!follower.isEmailNotificationsEnabled()) continue;
            if (follower.getEmail() == null || follower.getEmail().isBlank()) continue;
            try {
                emailService.sendNewEventEmail(
                        follower.getEmail(),
                        evento.getCreatore().getUsername(),
                        evento.getTitolo(),
                        when,
                        where
                );
            } catch (Exception e) {
                System.err.println("Errore email nuovo evento: " + e.getMessage());
            }
        }
    }

    private void notifySignup(Evento evento, AppUser studente) {
        if (studente == null || studente.getEmail() == null || studente.getEmail().isBlank()) return;
        if (!studente.isEmailNotificationsEnabled()) return;
        try {
            emailService.sendEventSignupEmail(
                    studente.getEmail(),
                    evento.getTitolo(),
                    formatDate(evento.getDataInizio()),
                    evento.getLuogo()
            );
        } catch (Exception e) {
            System.err.println("Errore email iscrizione evento: " + e.getMessage());
        }
    }

    private void notifyEventUpdate(Evento evento) {
        String when = formatDate(evento.getDataInizio());
        String where = evento.getLuogo();
        for (AppUser iscritto : evento.getIscritti()) {
            if (!iscritto.isEmailNotificationsEnabled()) continue;
            if (iscritto.getEmail() == null || iscritto.getEmail().isBlank()) continue;
            try {
                emailService.sendEventUpdateEmail(iscritto.getEmail(), evento.getTitolo(), when, where);
            } catch (Exception e) {
                System.err.println("Errore email aggiornamento evento: " + e.getMessage());
            }
        }
    }

    private String formatDate(LocalDateTime dt) {
        return dt == null ? "TBD" : dt.toString();
    }

    private boolean equalsNullable(String left, String right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        return left.equals(right);
    }

//    public EventoDettaglioDTO getEventoDettaglio(Long id, String username) {
//        Evento evento = eventoRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Evento non trovato con ID " + id));
//
//        return EventoMapper.toDTO(evento, username);
//    }

}
