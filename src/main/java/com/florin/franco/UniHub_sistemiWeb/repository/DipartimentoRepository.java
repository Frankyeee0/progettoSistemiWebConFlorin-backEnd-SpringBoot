package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Dipartimento;

@Repository
public interface DipartimentoRepository extends JpaRepository<Dipartimento, Long> {

    // ✅ Trova tutti i dipartimenti appartenenti a una certa università
    List<Dipartimento> findByUniversitaId(Long universitaId);
}