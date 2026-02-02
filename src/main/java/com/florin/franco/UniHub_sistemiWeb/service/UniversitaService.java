package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.dto.DipartimentoDto;
import com.florin.franco.UniHub_sistemiWeb.dto.UniversitaDto;
import com.florin.franco.UniHub_sistemiWeb.entity.Universita;
import com.florin.franco.UniHub_sistemiWeb.repository.UniversitaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UniversitaService {
	
	 @Autowired
	    private UniversitaRepository universitaRepository;

	    @Autowired
	    private ModelMapper modelMapper;

	  
	    public List<UniversitaDto> getAllUniversita() {
	        List<Universita> entityList = universitaRepository.findAll();

	        return entityList.stream()
	                .map(universita -> {
	                    UniversitaDto dto = modelMapper.map(universita, UniversitaDto.class);

	                    if (universita.getDipartimenti() != null) {
	                        List<DipartimentoDto> deps = universita.getDipartimenti().stream()
	                                .map(dep -> modelMapper.map(dep, DipartimentoDto.class))
	                                .collect(Collectors.toList());
	                        dto.setDipartimenti(deps);
	                    }

	                    return dto;
	                })
	                .collect(Collectors.toList());
	    }

	    public UniversitaDto getUniversitaById(Long id) {
	        Universita universita = universitaRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Università non trovata"));

	        UniversitaDto dto = modelMapper.map(universita, UniversitaDto.class);

	        if (universita.getDipartimenti() != null) {
	            List<DipartimentoDto> deps = universita.getDipartimenti().stream()
	                    .map(dep -> modelMapper.map(dep, DipartimentoDto.class))
	                    .collect(Collectors.toList());
	            dto.setDipartimenti(deps);
	        }

	        return dto;
	    }

	    public UniversitaDto createUniversita(String nome) {
	        if (nome == null || nome.trim().isEmpty()) {
	            throw new RuntimeException("Nome universita mancante");
	        }
	        String normalized = nome.trim();
	        if (universitaRepository.existsByNomeIgnoreCase(normalized)) {
	            throw new RuntimeException("Universita gia esistente");
	        }
	        Universita entity = new Universita();
	        entity.setNome(normalized);
	        Universita saved = universitaRepository.save(entity);
	        return modelMapper.map(saved, UniversitaDto.class);
	    }

	    public void deleteUniversita(Long id) {
	        if (id == null || !universitaRepository.existsById(id)) {
	            throw new RuntimeException("Universita non trovata");
	        }
	        universitaRepository.deleteById(id);
	    }
	}
