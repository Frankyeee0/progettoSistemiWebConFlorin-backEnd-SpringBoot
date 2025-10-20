package com.florin.franco.UniHub_sistemiWeb.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CommentoDto {

    private Long id;
    private String testo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCreazione;

    private Long eventoId;  // riferimento all'evento (se presente)
    private Long clubId;    // riferimento al club (se presente)

    private UserLiteDto autore;  // autore del commento (id, username)
}