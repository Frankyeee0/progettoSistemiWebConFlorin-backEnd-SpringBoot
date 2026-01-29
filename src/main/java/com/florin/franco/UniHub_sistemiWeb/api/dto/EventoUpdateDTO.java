package com.florin.franco.UniHub_sistemiWeb.api.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventoUpdateDTO {
    private String titolo;
    private String descrizione;
    private String categoria;
    private String universita;
    private String luogo;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private Integer postiTotali;
    private LocalDateTime deadlineIscrizione;
}
