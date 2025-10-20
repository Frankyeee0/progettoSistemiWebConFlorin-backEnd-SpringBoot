package com.florin.franco.UniHub_sistemiWeb.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class FeedbackDto {

    private Long id;
    private String testo;         // contenuto del feedback
    private int valutazione;      // punteggio o rating (es. 1–5 stelle)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCreazione;

    private Long eventoId;        // opzionale, se associato a un evento
    private Long clubId;          // opzionale, se associato a un club
    private UserLiteDto autore;   // utente che ha lasciato il feedback
}