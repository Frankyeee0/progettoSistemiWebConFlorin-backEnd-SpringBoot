package com.florin.franco.UniHub_sistemiWeb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventDetailDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventListDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoCreateDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoUpdateDTO;
import com.florin.franco.UniHub_sistemiWeb.api.mapper.EventoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private com.florin.franco.UniHub_sistemiWeb.repository.EventLikeRepository eventLikeRepository;

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private EmailService emailService;


    public EventDetailDTO creaEvento(EventoCreateDTO dto, Long creatoreId) {
        AppUser creatore = userRepository.findById(creatoreId)
                .orElseThrow(() -> new RuntimeException("Creatore non trovato"));

        if (creatore.getRole() != Ruolo.ADMIN && creatore.getRole() != Ruolo.USER) {
            throw new RuntimeException("Solo gli utenti possono creare eventi!");
        }

        Evento evento = EventoMapper.fromCreateDTO(dto);
        evento.setCreatore(creatore);
        if (creatore.getDipartimento() != null && creatore.getDipartimento().getUniversita() != null) {
            evento.setUniversita(creatore.getDipartimento().getUniversita().getNome());
        }

        Evento salvato = eventoRepository.save(evento);

        notifyFollowersNewEvent(salvato);

        String msg = "📢 Nuovo evento: " + salvato.getTitolo()
                + " | " + formatDate(salvato.getDataInizio())
                + (salvato.getLuogo() != null ? " | " + salvato.getLuogo() : "");
        messageService.broadcastMessage(creatore.getId(), msg, Ruolo.USER); // solo utenti

        List<UserLiteDto> iscrittiDto = salvato.getIscritti().stream()
                .map(u -> new UserLiteDto(u.getId(), u.getUsername(), u.getProfileImage()))
                .toList();
        long likeCount = eventLikeRepository.countByEvento(salvato);
        return EventoMapper.toDetailDTO(salvato, false, iscrittiDto, likeCount, false);
    }
    public List<EventListDTO> searchEvents(String search, LocalDateTime from, LocalDateTime to, Long userId) {
        return eventoRepository.searchEvents(search, from, to)
                .stream()
                .filter(e -> !e.isHidden())
                .map(e -> toListDTO(e, userId))
                .toList();
    }

    public List<EventListDTO> getAllEvents(
            String search,
            String category,
            String university,
            String start,
            String end,
            Long userId
    ) {
        LocalDateTime startDate = parseDate(start);
        LocalDateTime endDate = parseDate(end);
        String searchLower = search == null ? "" : search.trim().toLowerCase();
        String categoryLower = category == null ? "" : category.trim().toLowerCase();
        String universityLower = university == null ? "" : university.trim().toLowerCase();

        List<Evento> listEventsEntity = eventoRepository.findAll()
                .stream()
                .filter(e -> !e.isHidden())
                .filter(e -> matchesSearch(e, searchLower))
                .filter(e -> matchesCategory(e, categoryLower))
                .filter(e -> matchesUniversity(e, universityLower))
                .filter(e -> matchesDateRange(e, startDate, endDate))
                .toList();
        return listEventsEntity.stream()
                .map(e -> toListDTO(e, userId))
                .toList();
    }
    
    public EventDetailDTO iscriviStudente(Long eventoId, Long studenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        AppUser studente = userRepository.findById(studenteId)
                .orElseThrow(() -> new RuntimeException("Studente non trovato"));

        if (studente.getRole() != Ruolo.USER) {
            throw new RuntimeException("Solo gli utenti possono iscriversi agli eventi!");
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

        List<UserLiteDto> iscrittiDto = eventoAggiornato.getIscritti().stream()
                .map(u -> new UserLiteDto(u.getId(), u.getUsername(), u.getProfileImage()))
                .toList();

        notifySignup(evento, studente);
        long likeCount = eventLikeRepository.countByEvento(eventoAggiornato);
        boolean userLiked = eventLikeRepository.existsByEventoIdAndUserId(eventoAggiornato.getId(), studenteId);
        return EventoMapper.toDetailDTO(eventoAggiornato, true, iscrittiDto, likeCount, userLiked);
    }

    public EventDetailDTO disiscriviStudente(Long eventoId, Long studenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        AppUser studente = userRepository.findById(studenteId)
                .orElseThrow(() -> new RuntimeException("Studente non trovato"));

        evento.getIscritti().remove(studente);
        Evento salvato = eventoRepository.save(evento);
        List<UserLiteDto> iscrittiDto = salvato.getIscritti().stream()
                .map(u -> new UserLiteDto(u.getId(), u.getUsername(), u.getProfileImage()))
                .toList();
        boolean userIscritto = false;
        long likeCount = eventLikeRepository.countByEvento(salvato);
        boolean userLiked = eventLikeRepository.existsByEventoIdAndUserId(eventoId, studenteId);
        return EventoMapper.toDetailDTO(salvato, userIscritto, iscrittiDto, likeCount, userLiked);
    }
    
    public EventDetailDTO getEventDetails(Long id, Long userId) {
        Evento event = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        List<UserLiteDto> iscrittiDto = event.getIscritti().stream()
                .map(u -> new UserLiteDto(u.getId(), u.getUsername(), u.getProfileImage()))
                .toList();
        long likeCount = eventLikeRepository.countByEvento(event);
        boolean userLiked = userId != null && eventLikeRepository.existsByEventoIdAndUserId(id, userId);
        boolean userIscritto = userId != null && event.getIscritti().stream().anyMatch(u -> u.getId().equals(userId));
        return EventoMapper.toDetailDTO(event, userIscritto, iscrittiDto, likeCount, userLiked);
    }

    public EventDetailDTO aggiornaEvento(Long eventoId, EventoUpdateDTO dto, Long editorId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        if (editorId != null) {
            AppUser editor = userRepository.findById(editorId)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));
            if (editor.getRole() != Ruolo.ADMIN) {
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
        if (dto.getCategoria() != null) {
            evento.setCategoria(dto.getCategoria());
        }
        if (dto.getUniversita() != null) {
            evento.setUniversita(dto.getUniversita());
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

        List<UserLiteDto> iscrittiDto = salvato.getIscritti().stream()
                .map(u -> new UserLiteDto(u.getId(), u.getUsername(), u.getProfileImage()))
                .toList();
        long likeCount = eventLikeRepository.countByEvento(salvato);
        boolean userLiked = editorId != null && eventLikeRepository.existsByEventoIdAndUserId(eventoId, editorId);
        return EventoMapper.toDetailDTO(salvato, false, iscrittiDto, likeCount, userLiked);
    }

    public EventDetailDTO getEventoDettaglio(Long eventoId, Long userId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        boolean userIscritto = false;
        if (userId != null) {
            userIscritto = evento.getIscritti().stream()
                    .anyMatch(u -> u.getId().equals(userId));
        }

        return toDettaglioDTO(evento, userIscritto, userId);
    }

    private EventDetailDTO toDettaglioDTO(Evento evento, boolean userIscritto, Long userId) {
        List<UserLiteDto> iscrittiDto = evento.getIscritti().stream()
                .map(u -> new UserLiteDto(u.getId(), u.getUsername(), u.getProfileImage()))
                .toList();
        long likeCount = eventLikeRepository.countByEvento(evento);
        boolean userLiked = userId != null && eventLikeRepository.existsByEventoIdAndUserId(evento.getId(), userId);
        return EventoMapper.toDetailDTO(evento, userIscritto, iscrittiDto, likeCount, userLiked);
    }

    private boolean matchesSearch(Evento evento, String searchLower) {
        if (searchLower == null || searchLower.isBlank()) return true;
        String titolo = safeLower(evento.getTitolo());
        String descrizione = safeLower(evento.getDescrizione());
        String luogo = safeLower(evento.getLuogo());
        return titolo.contains(searchLower) || descrizione.contains(searchLower) || luogo.contains(searchLower);
    }

    private boolean matchesCategory(Evento evento, String categoryLower) {
        if (categoryLower == null || categoryLower.isBlank()) return true;
        String categoria = safeLower(evento.getCategoria());
        return categoria.equals(categoryLower);
    }

    private boolean matchesUniversity(Evento evento, String universityLower) {
        if (universityLower == null || universityLower.isBlank()) return true;
        String universita = safeLower(evento.getUniversita());
        return universita.contains(universityLower);
    }

    private boolean matchesDateRange(Evento evento, LocalDateTime start, LocalDateTime end) {
        if (start == null && end == null) return true;
        LocalDateTime dataInizio = evento.getDataInizio();
        if (dataInizio == null) return false;
        if (start != null && dataInizio.isBefore(start)) return false;
        if (end != null && dataInizio.isAfter(end)) return false;
        return true;
    }

    private EventListDTO toListDTO(Evento evento, Long userId) {
        long likeCount = eventLikeRepository.countByEvento(evento);
        boolean userLiked = userId != null && eventLikeRepository.existsByEventoIdAndUserId(evento.getId(), userId);
        return EventoMapper.toListDTO(evento, likeCount, userLiked);
    }

    public EventDetailDTO likeEvent(Long eventoId, Long userId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (!eventLikeRepository.existsByEventoIdAndUserId(eventoId, userId)) {
            com.florin.franco.UniHub_sistemiWeb.entity.EventLike like = new com.florin.franco.UniHub_sistemiWeb.entity.EventLike();
            like.setEvento(evento);
            like.setUser(user);
            eventLikeRepository.save(like);
        }

        return getEventDetails(eventoId, userId);
    }

    public EventDetailDTO unlikeEvent(Long eventoId, Long userId) {
        eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        eventLikeRepository.deleteByEventoIdAndUserId(eventoId, userId);
        return getEventDetails(eventoId, userId);
    }

    private LocalDateTime parseDate(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try {
            if (raw.endsWith("Z") || raw.contains("+")) {
                return java.time.OffsetDateTime.parse(raw).toLocalDateTime();
            }
            return LocalDateTime.parse(raw);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
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
