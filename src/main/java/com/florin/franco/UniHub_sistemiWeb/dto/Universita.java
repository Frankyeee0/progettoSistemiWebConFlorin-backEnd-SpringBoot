package com.florin.franco.UniHub_sistemiWeb.dto;

import java.util.List;

import com.florin.franco.UniHub_sistemiWeb.entity.AppUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Universita {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @Column(nullable = false, unique = true)
	    private String nome;

	    // ✅ Relazione 1→N con Dipartimento
	    @OneToMany(mappedBy = "universita", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Dipartimento> dipartimenti;
}
	 

