package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Commento;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Long> {

    List<Commento> findByEventoId(Long eventoId);

    List<Commento> findByClubId(Long clubId);
    
    @Query("SELECT c FROM Commento c WHERE c.autore.id = :autoreId ORDER BY c.dataCreazione DESC")
    List<Commento> findByAutoreId(@Param("autoreId") Long autoreId);
    
}