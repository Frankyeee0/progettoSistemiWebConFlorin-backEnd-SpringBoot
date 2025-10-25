package com.florin.franco.UniHub_sistemiWeb.api.mapper;

import com.florin.franco.UniHub_sistemiWeb.api.dto.*;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;

public class EventoMapper {

    // 🔹 Versione base: senza username (es. dopo creazione evento)
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
                toCreatoreDTO(e.getCreatore()),
                false 
        );
    }

    // 🔹 Versione con username: usata nel getEvento(id, username)
    public static EventoDettaglioDTO toDTO(Evento e, String username) {
        if (e == null) return null;

        boolean isUserIscritto = false;
        if (username != null && e.getIscritti() != null) {
            isUserIscritto = e.getIscritti().stream()
                    .anyMatch(u -> u.getUsername().equals(username));
        }

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
                toCreatoreDTO(e.getCreatore()),
                isUserIscritto
        );
    }

    // 🔹 DTO → Entity (per creazione evento)
    public static Evento fromCreateDTO(EventoCreateDTO dto) {
        if (dto == null) return null;

        Evento evento = new Evento();
        evento.setTitolo(dto.getTitolo());
        evento.setDescrizione(dto.getDescrizione());
        evento.setLuogo(dto.getLuogo());
        evento.setDataInizio(dto.getDataInizio());
        evento.setDataFine(dto.getDataFine());
        evento.setPostiTotali(dto.getPostiTotali());
        evento.setDeadlineIscrizione(dto.getDeadlineIscrizione());
        return evento;
    }

    private static CreatoreDTO toCreatoreDTO(AppUser u) {
        if (u == null) return null;

        return new CreatoreDTO(
                u.getId(),
                u.getUsername()
        );
    }
}
