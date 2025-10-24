package com.florin.franco.UniHub_sistemiWeb.repository;

import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByEventoId(Long eventoId);

    List<Feedback> findByClubId(Long clubId);
}