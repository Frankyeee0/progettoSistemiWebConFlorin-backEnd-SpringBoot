package com.florin.franco.UniHub_sistemiWeb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.florin.franco.UniHub_sistemiWeb.api.dto.CreatoreDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoCreateDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDettaglioDTO;
import com.florin.franco.UniHub_sistemiWeb.api.mapper.EventoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // 🔹 Creazione evento (solo Admin/SuperAdmin)
    public EventoDettaglioDTO creaEvento(EventoCreateDTO dto, Long creatoreId) {
        AppUser creatore = userRepository.findById(creatoreId)
                .orElseThrow(() -> new RuntimeException("Creatore non trovato"));

        if (creatore.getRole() != Ruolo.ADMIN && creatore.getRole() != Ruolo.SUPERADMIN) {
            throw new RuntimeException("Solo gli admin possono creare eventi!");
        }

        // 🔹 Mappa DTO → Entity
        Evento evento = EventoMapper.fromCreateDTO(dto);
        evento.setCreatore(creatore);

        // 🔹 Salva e restituisci DTO di dettaglio
        Evento salvato = eventoRepository.save(evento);
        return EventoMapper.toDTO(salvato);
    }

    // 🔹 Lista di tutti gli eventi
    public List<EventoDTO> getAllEventi() {
        List<Evento> eventi = eventoRepository.findAll();

        return eventi.stream().map(ev -> {
            CreatoreDTO creatoreDto = null;
            if (ev.getCreatore() != null) {
                creatoreDto = new CreatoreDTO(
                        ev.getCreatore().getId(),
                        ev.getCreatore().getUsername()
                );
            }

            return new EventoDTO(
                    ev.getId(),
                    ev.getTitolo(),
                    ev.getDescrizione(),
                    ev.getLuogo(),
                    ev.getDataInizio(),
                    creatoreDto
            );
        }).collect(Collectors.toList());
    }

    // 🔹 Iscrizione studente con controlli
    public Evento iscriviStudente(Long eventoId, Long studenteId) {
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
        return eventoRepository.save(evento);
    }

    // 🔹 Disiscrizione studente
    public Evento disiscriviStudente(Long eventoId, Long studenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        AppUser studente = userRepository.findById(studenteId)
                .orElseThrow(() -> new RuntimeException("Studente non trovato"));

        evento.getIscritti().remove(studente);
        return eventoRepository.save(evento);
    }

    public EventoDettaglioDTO getEventoDettaglio(Long id, String username) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato con ID " + id));

        return EventoMapper.toDTO(evento, username);
    }

}