package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.dto.Universita;

@Repository
public interface UniversitaRepository extends JpaRepository<Universita, Long> {

	// 🔹 1. Trova università per nome esatto
    Optional<Universita> findByNome(String nome);
    // → SELECT * FROM universita WHERE nome = :nome

    // 🔹 2. Trova università con nome che contiene una parola
    List<Universita> findByNomeContainingIgnoreCase(String parteNome);
    // → SELECT * FROM universita WHERE LOWER(nome) LIKE LOWER('%parteNome%')
}