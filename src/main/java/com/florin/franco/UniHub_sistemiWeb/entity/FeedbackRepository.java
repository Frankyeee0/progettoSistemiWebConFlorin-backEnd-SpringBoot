package com.florin.franco.UniHub_sistemiWeb.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByEventoId(Long eventoId);

    List<Feedback> findByClubId(Long clubId);
}