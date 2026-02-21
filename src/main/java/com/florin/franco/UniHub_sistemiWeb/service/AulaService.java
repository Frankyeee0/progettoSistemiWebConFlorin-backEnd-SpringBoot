package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.AulaDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Aula;
import com.florin.franco.UniHub_sistemiWeb.entity.Universita;
import com.florin.franco.UniHub_sistemiWeb.repository.AulaRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;

@Service
public class AulaService {

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private UniversitaRepository universitaRepository;

    public List<AulaDto> getAll() {
        return aulaRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public AulaDto create(AulaDto payload) {
        validatePayload(payload);
        Aula aula = new Aula();
        aula.setNome(payload.getNome().trim());
        aula.setEdificio(normalize(payload.getEdificio()));
        aula.setCapienza(payload.getCapienza());
        aula.setUniversita(resolveUniversita(payload.getUniversitaId()));
        Aula saved = aulaRepository.save(aula);
        return toDto(saved);
    }

    public AulaDto update(Long id, AulaDto payload) {
        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula non trovata"));

        if (payload.getNome() != null && !payload.getNome().trim().isEmpty()) {
            aula.setNome(payload.getNome().trim());
        }
        if (payload.getEdificio() != null) {
            aula.setEdificio(normalize(payload.getEdificio()));
        }
        if (payload.getCapienza() != null) {
            aula.setCapienza(payload.getCapienza());
        }
        if (payload.getUniversitaId() != null) {
            aula.setUniversita(resolveUniversita(payload.getUniversitaId()));
        }

        Aula saved = aulaRepository.save(aula);
        return toDto(saved);
    }

    public void delete(Long id) {
        if (!aulaRepository.existsById(id)) {
            throw new RuntimeException("Aula non trovata");
        }
        aulaRepository.deleteById(id);
    }

    private void validatePayload(AulaDto payload) {
        if (payload == null || payload.getNome() == null || payload.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome aula mancante");
        }
    }

    private Universita resolveUniversita(Long universitaId) {
        if (universitaId == null) {
            return null;
        }
        return universitaRepository.findById(universitaId)
                .orElseThrow(() -> new RuntimeException("Universita non trovata"));
    }

    private AulaDto toDto(Aula aula) {
        AulaDto dto = new AulaDto();
        dto.setId(aula.getId());
        dto.setNome(aula.getNome());
        dto.setEdificio(aula.getEdificio());
        dto.setCapienza(aula.getCapienza());
        dto.setUniversitaId(aula.getUniversita() != null ? aula.getUniversita().getId() : null);
        return dto;
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
