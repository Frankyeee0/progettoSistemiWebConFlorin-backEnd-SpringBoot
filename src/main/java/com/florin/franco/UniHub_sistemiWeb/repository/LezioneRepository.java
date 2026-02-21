package com.florin.franco.UniHub_sistemiWeb.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.florin.franco.UniHub_sistemiWeb.entity.Lezione;

@Repository
public interface LezioneRepository extends JpaRepository<Lezione, Long> {
    boolean existsByAulaIdAndGiornoSettimanaAndOraInizioLessThanAndOraFineGreaterThan(
            Long aulaId,
            String giornoSettimana,
            LocalTime oraFine,
            LocalTime oraInizio
    );

    boolean existsByAulaIdAndGiornoSettimanaAndOraInizioLessThanAndOraFineGreaterThanAndIdNot(
            Long aulaId,
            String giornoSettimana,
            LocalTime oraFine,
            LocalTime oraInizio,
            Long id
    );

    List<Lezione> findByMateriaId(Long materiaId);

    List<Lezione> findByAulaId(Long aulaId);

    List<Lezione> findByGiornoSettimana(String giornoSettimana);
}
