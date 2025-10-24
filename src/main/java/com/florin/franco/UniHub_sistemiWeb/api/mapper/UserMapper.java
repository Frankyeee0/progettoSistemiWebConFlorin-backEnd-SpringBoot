package com.florin.franco.UniHub_sistemiWeb.api.mapper;

import com.florin.franco.UniHub_sistemiWeb.api.dto.CreatoreDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.EventoDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.UserCardDTO;
import com.florin.franco.UniHub_sistemiWeb.api.dto.UserProfileDTO;
import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;
import com.florin.franco.UniHub_sistemiWeb.entity.Evento;

import java.util.List;

public class UserMapper {

    public static UserCardDTO toCardDTO(AppUser u, boolean following) {
        if (u == null) return null;

        String faculty = u.getDipartimento() != null ? u.getDipartimento().getNome() : null;

        return new UserCardDTO(
                u.getId(),
                u.getName(),
                u.getSurname(),
                u.getUsername(),
                u.getRole() != null ? u.getRole().name() : null,
                faculty,
                following
        );
    }

    public static UserProfileDTO toProfileDTO(
            AppUser u,
            boolean following,
            long followerCount,
            long followingCount,
            List<Evento> recent
    ) {
        String university = null;
        String faculty = null;

        if (u.getDipartimento() != null) {
            faculty = u.getDipartimento().getNome();
            if (u.getDipartimento().getUniversita() != null) {
                university = u.getDipartimento().getUniversita().getNome();
            }
        }

        List<EventoDTO> ev = (recent == null || recent.isEmpty()) ? List.of() :
                recent.stream()
                        .map(e -> new EventoDTO(
                                e.getId(),
                                e.getTitolo(),
                                e.getDescrizione(),
                                e.getLuogo(),
                                e.getDataInizio(),
                                new CreatoreDTO(u.getId(), u.getUsername())
                        ))
                        .toList();

        return new UserProfileDTO(
                u.getId(),
                u.getName(),
                u.getSurname(),
                u.getUsername(),
                u.getRole() != null ? u.getRole().name() : null,
                u.getEmail(),
                university,
                faculty,
                followerCount,
                followingCount,
                following,
                ev
        );
    }
}
