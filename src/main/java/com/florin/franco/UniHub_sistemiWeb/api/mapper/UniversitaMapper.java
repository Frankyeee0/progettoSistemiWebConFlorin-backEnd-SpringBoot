package com.florin.franco.UniHub_sistemiWeb.api.mapper;

import com.florin.franco.UniHub_sistemiWeb.api.dto.*;
import com.florin.franco.UniHub_sistemiWeb.dto.Universita;
import com.florin.franco.UniHub_sistemiWeb.dto.Dipartimento;
import java.util.List;

public final class UniversitaMapper {
    private UniversitaMapper() {}

    public static UniversitaDTO toDto(Universita u) {
        List<DipartimentoDTO> deps = u.getDipartimenti()
                .stream()
                .map(d -> new DipartimentoDTO(d.getId(), d.getNome()))
                .toList();
        return new UniversitaDTO(u.getId(), u.getNome(), deps);
    }
}
