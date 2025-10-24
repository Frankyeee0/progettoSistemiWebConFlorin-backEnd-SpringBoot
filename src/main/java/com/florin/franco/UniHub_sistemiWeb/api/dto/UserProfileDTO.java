package com.florin.franco.UniHub_sistemiWeb.api.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String role;
    private String email;
    private String university;
    private String faculty;
    private long followerCount;
    private long followingCount;
    private boolean following;
    private List<EventoDTO> recentEvents; // ✅ qui usi direttamente il tuo DTO
}

