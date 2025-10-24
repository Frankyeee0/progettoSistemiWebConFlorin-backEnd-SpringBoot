package com.florin.franco.UniHub_sistemiWeb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserCardDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String role;        // Ruolo come stringa (ADMIN/STUDENT/...)
    private String faculty;     // Nome dipartimento se presente
    private boolean following;  // se l'utente corrente lo segue già
}
