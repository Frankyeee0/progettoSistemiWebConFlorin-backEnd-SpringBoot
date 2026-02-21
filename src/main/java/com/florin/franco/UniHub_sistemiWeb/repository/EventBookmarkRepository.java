package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.EventBookmark;

@Repository
public interface EventBookmarkRepository extends JpaRepository<EventBookmark, Long> {
    boolean existsByEventoIdAndUserId(Long eventoId, Long userId);
    void deleteByEventoIdAndUserId(Long eventoId, Long userId);
    List<EventBookmark> findByUserId(Long userId);
}
