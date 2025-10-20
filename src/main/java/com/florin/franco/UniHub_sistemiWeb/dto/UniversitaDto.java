package com.florin.franco.UniHub_sistemiWeb.dto;

import lombok.Data;
import java.util.List;

@Data
public class UniversitaDto {

    private Long id;
    private String nome;
    private List<DipartimentoDto> dipartimenti;
    
}