package com.florin.franco.UniHub_sistemiWeb.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ClubDettaglioDTO(
        Long id,
        String nome,
        String descrizione,
        int maxMembri,
        int postiDisponibili,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime dataCreazione,
        String fondatore
) {}
