package com.florin.franco.UniHub_sistemiWeb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String studentId;
    private Long dipartimentoId;
    private Boolean emailNotificationsEnabled;
}
