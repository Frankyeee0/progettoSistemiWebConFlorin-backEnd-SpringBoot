package com.florin.franco.UniHub_sistemiWeb.dto;

import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Ruolo  role;
    private String email;
}