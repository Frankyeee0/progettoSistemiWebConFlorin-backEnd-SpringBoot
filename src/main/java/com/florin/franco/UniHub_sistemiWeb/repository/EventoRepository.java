package com.florin.franco.UniHub_sistemiWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Evento;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findTop6ByCreatore_IdOrderByDataInizioDesc(Long creatoreId);

    @Query("""
        SELECT e
        FROM Evento e
        WHERE e.hidden = false
          AND (:search IS NULL OR :search = '' OR
               LOWER(e.titolo) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(e.descrizione) LIKE LOWER(CONCAT('%', :search, '%')))
          AND e.dataInizio >= COALESCE(:from, e.dataInizio)
          AND e.dataInizio <= COALESCE(:to, e.dataInizio)
        ORDER BY e.dataInizio DESC
    """)
    List<Evento> searchEvents(
            @Param("search") String search,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}