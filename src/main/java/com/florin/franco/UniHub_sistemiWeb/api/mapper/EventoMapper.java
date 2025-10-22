package com.florin.franco.UniHub_sistemiWeb.api.mapper;

import com.florin.franco.UniHub_sistemiWeb.api.dto.*;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;

public class EventoMapper {

    public static EventoDettaglioDTO toDTO(Evento e) {
        if (e == null) return null;

        return new EventoDettaglioDTO(
                e.getId(),
                e.getTitolo(),
                e.getDescrizione(),
                e.getDataInizio(),
                e.getDataFine(),
                e.getLuogo(),
                e.getPostiTotali(),
                e.getPostiDisponibili(),
                e.getDeadlineIscrizione(),
                toCreatoreDTO(e.getCreatore())
        );
    }

    private static CreatoreDTO toCreatoreDTO(AppUser u) {
        if (u == null) return null;

        return new CreatoreDTO(
                u.getId(),
                u.getUsername(),
                u.getRole().name()
        );
    }
}
