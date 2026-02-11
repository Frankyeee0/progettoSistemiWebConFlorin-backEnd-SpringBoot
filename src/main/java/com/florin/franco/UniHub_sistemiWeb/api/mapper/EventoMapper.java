package com.florin.franco.UniHub_sistemiWeb.api.mapper;

import com.florin.franco.UniHub_sistemiWeb.api.dto.CreatoreDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventDetailDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventListDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoCreateDTO;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.dto.UserLiteDto;
import java.util.List;

public class EventoMapper {

    // 🔹 Versione base: senza username (es. dopo creazione evento)
    public static EventDetailDTO toDetailDTO(
            Evento e,
            boolean userIscritto,
            List<UserLiteDto> iscritti,
            long likeCount,
            boolean userLiked
    ) {
        if (e == null) return null;

        return new EventDetailDTO(
                e.getId(),
                e.getTitolo(),
                e.getDescrizione(),
                e.getCategoria(),
                e.getUniversita(),
                e.getDataInizio(),
                e.getDataFine(),
                e.getLuogo(),
                e.getPostiTotali(),
                e.getPostiDisponibili(),
                e.getDeadlineIscrizione(),
                toCreatoreDTO(e.getCreatore()),
                userIscritto,
                iscritti,
                likeCount,
                userLiked
        );
    }

    public static EventListDTO toListDTO(Evento e, long likeCount, boolean userLiked) {
        if (e == null) return null;
        return new EventListDTO(
                e.getId(),
                e.getTitolo(),
                e.getDescrizione(),
                e.getCategoria(),
                e.getUniversita(),
                e.getLuogo(),
                e.getDataInizio(),
                toCreatoreDTO(e.getCreatore()),
                likeCount,
                userLiked
        );
    }

    // 🔹 DTO → Entity (per creazione evento)
    public static Evento fromCreateDTO(EventoCreateDTO dto) {
        if (dto == null) return null;

        Evento evento = new Evento();
        evento.setTitolo(dto.getTitolo());
        evento.setDescrizione(dto.getDescrizione());
        evento.setCategoria(dto.getCategoria());
        evento.setUniversita(dto.getUniversita());
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
