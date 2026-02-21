package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.MateriaDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.entity.Materia;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.MateriaRepository;

@Service
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private DipartimentoRepository dipartimentoRepository;

    public List<MateriaDto> getAll() {
        return materiaRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public MateriaDto create(MateriaDto payload) {
        validatePayload(payload);
        String codice = payload.getCodice().trim();
        if (materiaRepository.existsByCodiceIgnoreCase(codice)) {
            throw new RuntimeException("Codice materia già esistente");
        }

        Materia materia = new Materia();
        materia.setNome(payload.getNome().trim());
        materia.setCodice(codice);
        materia.setCorsoDiStudi(normalize(payload.getCorsoDiStudi()));
        materia.setDipartimento(resolveDipartimento(payload.getDipartimentoId()));

        Materia saved = materiaRepository.save(materia);
        return toDto(saved);
    }

    public MateriaDto update(Long id, MateriaDto payload) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia non trovata"));

        if (payload.getNome() != null && !payload.getNome().trim().isEmpty()) {
            materia.setNome(payload.getNome().trim());
        }
        if (payload.getCodice() != null && !payload.getCodice().trim().isEmpty()) {
            String codice = payload.getCodice().trim();
            if (!codice.equalsIgnoreCase(materia.getCodice())
                    && materiaRepository.existsByCodiceIgnoreCase(codice)) {
                throw new RuntimeException("Codice materia già esistente");
            }
            materia.setCodice(codice);
        }
        if (payload.getCorsoDiStudi() != null) {
            materia.setCorsoDiStudi(normalize(payload.getCorsoDiStudi()));
        }
        if (payload.getDipartimentoId() != null) {
            materia.setDipartimento(resolveDipartimento(payload.getDipartimentoId()));
        }

        Materia saved = materiaRepository.save(materia);
        return toDto(saved);
    }

    public void delete(Long id) {
        if (!materiaRepository.existsById(id)) {
            throw new RuntimeException("Materia non trovata");
        }
        materiaRepository.deleteById(id);
    }

    private void validatePayload(MateriaDto payload) {
        if (payload == null || payload.getNome() == null || payload.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome materia mancante");
        }
        if (payload.getCodice() == null || payload.getCodice().trim().isEmpty()) {
            throw new RuntimeException("Codice materia mancante");
        }
    }

    private Dipartimento resolveDipartimento(Long dipartimentoId) {
        if (dipartimentoId == null) {
            return null;
        }
        return dipartimentoRepository.findById(dipartimentoId)
                .orElseThrow(() -> new RuntimeException("Dipartimento non trovato"));
    }

    private MateriaDto toDto(Materia materia) {
        MateriaDto dto = new MateriaDto();
        dto.setId(materia.getId());
        dto.setNome(materia.getNome());
        dto.setCodice(materia.getCodice());
        dto.setCorsoDiStudi(materia.getCorsoDiStudi());
        dto.setDipartimentoId(materia.getDipartimento() != null ? materia.getDipartimento().getId() : null);
        return dto;
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
