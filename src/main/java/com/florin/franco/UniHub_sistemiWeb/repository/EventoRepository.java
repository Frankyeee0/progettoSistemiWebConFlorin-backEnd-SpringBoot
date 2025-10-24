package com.florin.franco.UniHub_sistemiWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Evento;

import java.util.List;


@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findTop6ByCreatore_IdOrderByDataInizioDesc(Long creatoreId);

}