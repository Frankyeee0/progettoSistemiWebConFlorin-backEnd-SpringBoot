package com.florin.franco.UniHub_sistemiWeb.dto;

import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;
    private String surname;
    private String studentId;
    private String username;
    private String password;
    private String email;


    private Long universitaId;       // 👈 ID dell'università scelta
    private Long dipartimentoId;     // 👈 ID del dipartimento scelto

    private byte[] profileImage;     // 👈 immagine profilo opzionale
}
