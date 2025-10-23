package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Commento;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Long> {

    List<Commento> findByEventoId(Long eventoId);

    List<Commento> findByClubId(Long clubId);
    
}