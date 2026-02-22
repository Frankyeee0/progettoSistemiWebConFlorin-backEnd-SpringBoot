package com.florin.franco.UniHub_sistemiWeb.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.LezioneDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Aula;
import com.florin.franco.UniHub_sistemiWeb.entity.Lezione;
import com.florin.franco.UniHub_sistemiWeb.entity.Materia;
import com.florin.franco.UniHub_sistemiWeb.repository.AulaRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.LezioneRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.MateriaRepository;

@Service
public class LezioneService {

    @Autowired
    private LezioneRepository lezioneRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private AulaRepository aulaRepository;

    public List<LezioneDto> getAll(Long materiaId, Long aulaId, LocalDate data) {
        return lezioneRepository.findAll().stream()
                .filter(lezione -> materiaId == null || lezione.getMateria().getId().equals(materiaId))
                .filter(lezione -> aulaId == null || lezione.getAula().getId().equals(aulaId))
                .filter(lezione -> data == null || data.equals(lezione.getData()))
                .map(this::toDto)
                .toList();
    }

    public LezioneDto create(LezioneDto payload) {
        validatePayload(payload);
        Materia materia = materiaRepository.findById(payload.getMateriaId())
                .orElseThrow(() -> new RuntimeException("Materia non trovata"));
        Aula aula = aulaRepository.findById(payload.getAulaId())
                .orElseThrow(() -> new RuntimeException("Aula non trovata"));

        LocalDate data = payload.getData();
        LocalTime oraInizio = payload.getOraInizio();
        LocalTime oraFine = payload.getOraFine();
        validateTime(oraInizio, oraFine);
        validateOverlap(null, aula.getId(), data, oraInizio, oraFine);

        Lezione lezione = new Lezione();
        lezione.setMateria(materia);
        lezione.setAula(aula);
        lezione.setDocente(normalize(payload.getDocente()));
        lezione.setData(data);
        lezione.setOraInizio(oraInizio);
        lezione.setOraFine(oraFine);
        lezione.setNote(normalize(payload.getNote()));

        Lezione saved = lezioneRepository.save(lezione);
        return toDto(saved);
    }

    public LezioneDto update(Long id, LezioneDto payload) {
        Lezione lezione = lezioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lezione non trovata"));

        if (payload.getMateriaId() != null) {
            Materia materia = materiaRepository.findById(payload.getMateriaId())
                    .orElseThrow(() -> new RuntimeException("Materia non trovata"));
            lezione.setMateria(materia);
        }
        if (payload.getAulaId() != null) {
            Aula aula = aulaRepository.findById(payload.getAulaId())
                    .orElseThrow(() -> new RuntimeException("Aula non trovata"));
            lezione.setAula(aula);
        }
        if (payload.getDocente() != null) {
            lezione.setDocente(normalize(payload.getDocente()));
        }
        if (payload.getData() != null) {
            lezione.setData(payload.getData());
        }
        if (payload.getOraInizio() != null) {
            lezione.setOraInizio(payload.getOraInizio());
        }
        if (payload.getOraFine() != null) {
            lezione.setOraFine(payload.getOraFine());
        }
        if (payload.getNote() != null) {
            lezione.setNote(normalize(payload.getNote()));
        }

        validateTime(lezione.getOraInizio(), lezione.getOraFine());
        validateOverlap(
                lezione.getId(),
                lezione.getAula().getId(),
                lezione.getData(),
                lezione.getOraInizio(),
                lezione.getOraFine()
        );

        Lezione saved = lezioneRepository.save(lezione);
        return toDto(saved);
    }

    public void delete(Long id) {
        if (!lezioneRepository.existsById(id)) {
            throw new RuntimeException("Lezione non trovata");
        }
        lezioneRepository.deleteById(id);
    }

    private void validatePayload(LezioneDto payload) {
        if (payload == null || payload.getMateriaId() == null) {
            throw new RuntimeException("Materia mancante");
        }
        if (payload.getAulaId() == null) {
            throw new RuntimeException("Aula mancante");
        }
        if (payload.getData() == null) {
            throw new RuntimeException("Data mancante");
        }
        if (payload.getOraInizio() == null || payload.getOraFine() == null) {
            throw new RuntimeException("Orario mancante");
        }
    }

    private void validateTime(LocalTime oraInizio, LocalTime oraFine) {
        if (oraInizio == null || oraFine == null || !oraFine.isAfter(oraInizio)) {
            throw new RuntimeException("Orario non valido");
        }
    }

    private void validateOverlap(Long lezioneId, Long aulaId, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        boolean exists = lezioneId == null
                ? lezioneRepository.existsByAulaIdAndDataAndOraInizioLessThanAndOraFineGreaterThan(
                        aulaId, data, oraFine, oraInizio
                )
                : lezioneRepository.existsByAulaIdAndDataAndOraInizioLessThanAndOraFineGreaterThanAndIdNot(
                        aulaId, data, oraFine, oraInizio, lezioneId
                );
        if (exists) {
            throw new RuntimeException("Aula già occupata in quell'orario");
        }
    }

    private LezioneDto toDto(Lezione lezione) {
        LezioneDto dto = new LezioneDto();
        dto.setId(lezione.getId());
        dto.setMateriaId(lezione.getMateria().getId());
        dto.setMateriaNome(lezione.getMateria().getNome());
        dto.setAulaId(lezione.getAula().getId());
        dto.setAulaNome(lezione.getAula().getNome());
        dto.setDocente(lezione.getDocente());
        dto.setData(lezione.getData());
        dto.setOraInizio(lezione.getOraInizio());
        dto.setOraFine(lezione.getOraFine());
        dto.setNote(lezione.getNote());
        return dto;
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
