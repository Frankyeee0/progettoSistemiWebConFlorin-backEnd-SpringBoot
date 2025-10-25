package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.DipartimentoDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.entity.Universita;
import com.florin.franco.UniHub_sistemiWeb.repository.DipartimentoRepository;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DipartimentoService {
	
	@Autowired
    private  DipartimentoRepository dipartimentoRepository;
	@Autowired
    private  UniversitaRepository universitaRepository;
	@Autowired
    private  ModelMapper modelMapper;

    public List<DipartimentoDto> getDipartimentiByUniversita(Long universitaId) {
        List<Dipartimento> dipartimenti = dipartimentoRepository.findByUniversitaId(universitaId);

        return dipartimenti.stream()
                .map(dep -> modelMapper.map(dep, DipartimentoDto.class))
                .collect(Collectors.toList());
    }

    public DipartimentoDto createDipartimento(Long universitaId, DipartimentoDto dto) {
        Universita universita = universitaRepository.findById(universitaId)
                .orElseThrow(() -> new RuntimeException("Università non trovata con ID " + universitaId));

        Dipartimento entity = modelMapper.map(dto, Dipartimento.class);
        entity.setUniversita(universita);

        Dipartimento salvato = dipartimentoRepository.save(entity);
        return modelMapper.map(salvato, DipartimentoDto.class);
    }

    public DipartimentoDto updateDipartimento(Long dipartimentoId, DipartimentoDto dto) {
        Dipartimento dip = dipartimentoRepository.findById(dipartimentoId)
                .orElseThrow(() -> new RuntimeException("Dipartimento non trovato con ID " + dipartimentoId));

        dip.setNome(dto.getNome());

        Dipartimento aggiornato = dipartimentoRepository.save(dip);
        return modelMapper.map(aggiornato, DipartimentoDto.class);
    }

    public void deleteDipartimento(Long dipartimentoId) {
        Dipartimento dip = dipartimentoRepository.findById(dipartimentoId)
                .orElseThrow(() -> new RuntimeException("Dipartimento non trovato con ID " + dipartimentoId));

        dipartimentoRepository.delete(dip);
    }
}