package com.florin.franco.UniHub_sistemiWeb.api.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventoCreateDTO {
    private String titolo;
    private String descrizione;
    private String luogo;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private int postiTotali;
    private LocalDateTime deadlineIscrizione;
}

