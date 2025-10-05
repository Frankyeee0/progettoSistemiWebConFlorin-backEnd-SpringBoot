package com.florin.franco.UniHub_sistemiWeb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	
	@Column(nullable=false)
	private String role;
}
