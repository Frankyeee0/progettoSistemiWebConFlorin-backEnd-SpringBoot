package com.florin.franco.UniHub_sistemiWeb.dto;

import lombok.Data;

@Data
public class SupportRequest {
    private String name;
    private String email;
    private String subject;
    private String message;
}
