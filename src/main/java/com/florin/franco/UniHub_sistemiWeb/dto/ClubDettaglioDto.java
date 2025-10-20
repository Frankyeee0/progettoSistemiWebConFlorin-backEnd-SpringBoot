package com.florin.franco.UniHub_sistemiWeb.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    private List<UserLiteDto> membri;
}
