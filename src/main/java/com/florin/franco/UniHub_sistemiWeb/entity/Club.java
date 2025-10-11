package com.florin.franco.UniHub_sistemiWeb.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Club {
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @Column(nullable = false, unique = true)
	    private String nome;

	    private String descrizione;
	    private int maxMembri;
	    private LocalDateTime dataCreazione = LocalDateTime.now();

	    @ManyToOne
	    private AppUser fondatore;

	    @ManyToMany
	    @JoinTable(
	        name = "club_iscrizioni",
	        joinColumns = @JoinColumn(name = "club_id"),
	        inverseJoinColumns = @JoinColumn(name = "utente_id")
	    )
	    private Set<AppUser> membri = new HashSet<>();

	    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
	    @JsonManagedReference
	    private Set<Commento> commenti = new HashSet<>();

	    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
	    @JsonManagedReference
	    private Set<Feedback> feedbacks = new HashSet<>();

	    @Transient
	    public int getPostiDisponibili() {
	        return maxMembri - membri.size();
	    }
}