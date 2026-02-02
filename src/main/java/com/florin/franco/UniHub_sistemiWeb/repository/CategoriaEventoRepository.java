package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.CategoriaEvento;

@Repository
public interface CategoriaEventoRepository extends JpaRepository<CategoriaEvento, Long> {
    boolean existsByNomeIgnoreCase(String nome);
    List<CategoriaEvento> findAllByOrderByNomeAsc();
}
