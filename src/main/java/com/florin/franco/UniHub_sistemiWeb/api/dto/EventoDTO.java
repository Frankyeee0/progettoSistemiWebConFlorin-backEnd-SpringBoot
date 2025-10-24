package com.florin.franco.UniHub_sistemiWeb.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {
    private Long id;
    private String titolo;
    private String descrizione;
    private String luogo;
    private LocalDateTime dataInizio;

    private CreatoreDTO creatore; // nested DTO
}
