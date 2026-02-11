package com.florin.franco.UniHub_sistemiWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.EventLike;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {
    long countByEvento(Evento evento);
    boolean existsByEventoIdAndUserId(Long eventoId, Long userId);
    void deleteByEventoIdAndUserId(Long eventoId, Long userId);
}
