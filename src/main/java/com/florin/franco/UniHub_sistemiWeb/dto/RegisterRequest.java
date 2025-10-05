package com.florin.franco.UniHub_sistemiWeb.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role;
}