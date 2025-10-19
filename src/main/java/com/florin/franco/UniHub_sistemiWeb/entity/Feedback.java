package com.florin.franco.UniHub_sistemiWeb.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private int rating; // da 1 a 5

    @Column(length = 500)
    private String commento;

    private LocalDateTime dataCreazione = LocalDateTime.now();

    @ManyToOne
    private AppUser autore;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    @JsonBackReference("evento-feedbacks")
    private Evento evento;
;

    @ManyToOne
    @JsonBackReference
    private Club club;
}
