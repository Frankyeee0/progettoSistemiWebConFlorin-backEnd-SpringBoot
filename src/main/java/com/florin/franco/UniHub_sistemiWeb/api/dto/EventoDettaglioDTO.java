package com.florin.franco.UniHub_sistemiWeb.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDettaglioDTO {
    private Long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private String luogo;
    private int postiTotali;
    private int postiDisponibili;
    private LocalDateTime deadlineIscrizione;

    private CreatoreDTO creatore;
}
