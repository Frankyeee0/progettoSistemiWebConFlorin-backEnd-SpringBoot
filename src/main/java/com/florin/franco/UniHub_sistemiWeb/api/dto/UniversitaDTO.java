package com.florin.franco.UniHub_sistemiWeb.api.dto;

import java.util.List;

public record UniversitaDTO(Long id, String nome, List<DipartimentoDTO> dipartimenti) {}
