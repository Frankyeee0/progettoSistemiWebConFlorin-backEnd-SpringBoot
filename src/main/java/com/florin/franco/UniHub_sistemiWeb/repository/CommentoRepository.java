package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Commento;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Long> {

    // 🔹 Recupera tutti i commenti relativi a un evento
    List<Commento> findByEventoId(Long eventoId);

    // 🔹 Recupera tutti i commenti relativi a un club
    List<Commento> findByClubId(Long clubId);
}