package com.florin.franco.UniHub_sistemiWeb.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dipartimento {

	
	   
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @Column(nullable = false, unique = false)
	    private String nome;
	    
	    @ManyToOne
	    @JoinColumn(name = "universita_id", nullable = false)
	    private Universita universita;
	    
	    @OneToMany(mappedBy = "dipartimento", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<AppUser> studenti;
}

