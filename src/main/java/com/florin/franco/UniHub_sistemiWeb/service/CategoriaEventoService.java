package com.florin.franco.UniHub_sistemiWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.florin.franco.UniHub_sistemiWeb.entity.CategoriaEvento;
import com.florin.franco.UniHub_sistemiWeb.repository.CategoriaEventoRepository;

@Service
public class CategoriaEventoService {

    @Autowired
    private CategoriaEventoRepository categoriaEventoRepository;

    public List<CategoriaEvento> listAll() {
        return categoriaEventoRepository.findAllByOrderByNomeAsc();
    }

    public CategoriaEvento create(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RuntimeException("Nome categoria mancante");
        }
        String normalized = nome.trim().toLowerCase();
        if (categoriaEventoRepository.existsByNomeIgnoreCase(normalized)) {
            throw new RuntimeException("Categoria gia esistente");
        }
        CategoriaEvento cat = new CategoriaEvento();
        cat.setNome(normalized);
        return categoriaEventoRepository.save(cat);
    }

    public void delete(Long id) {
        if (id == null || !categoriaEventoRepository.existsById(id)) {
            throw new RuntimeException("Categoria non trovata");
        }
        categoriaEventoRepository.deleteById(id);
    }
}
