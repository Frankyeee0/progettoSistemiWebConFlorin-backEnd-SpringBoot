package com.florin.franco.UniHub_sistemiWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    boolean existsByCodiceIgnoreCase(String codice);
}
