package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Universita;

@Repository
public interface UniversitaRepository extends JpaRepository<Universita, Long> {

    Optional<Universita> findByNome(String nome);

    List<Universita> findByNomeContainingIgnoreCase(String parteNome);
}