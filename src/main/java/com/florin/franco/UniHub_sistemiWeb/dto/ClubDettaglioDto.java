package com.florin.franco.UniHub_sistemiWeb.dto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubDettaglioDto {
    private Long id;
    private String nome;
    private String descrizione;
    private String fondatoreUsername;
    private int postiDisponibili;
    private LocalDateTime dataCreazione;
    private List<UserLiteDto> membri;
}
