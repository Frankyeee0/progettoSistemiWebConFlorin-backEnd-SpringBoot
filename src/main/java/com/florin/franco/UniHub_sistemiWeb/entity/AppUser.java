package com.florin.franco.UniHub_sistemiWeb.entity;

import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Entity
@Data
@AllArgsConstructor 
@NoArgsConstructor
public class AppUser {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable=false, unique=true)
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo role;
	
	@Column(nullable = false, unique = true)
    private String email;
	
	@Column(name = "immagine_profilo_url")
	private String immagineProfiloUrl;
}
