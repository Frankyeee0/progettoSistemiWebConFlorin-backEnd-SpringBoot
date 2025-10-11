package com.florin.franco.UniHub_sistemiWeb.entity;

import java.util.HashSet;
import java.util.Set;

import com.florin.franco.UniHub_sistemiWeb.dto.Dipartimento;
import com.florin.franco.UniHub_sistemiWeb.dto.Universita;
import com.florin.franco.UniHub_sistemiWeb.utils.Ruolo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String studentId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo role;

    @Column(nullable = false, unique = true)
    private String email;

    @Lob
    @Column(name = "immagineProfilo")
    private byte[] immagineProfilo;

    // ✅ Molti studenti → 1 dipartimento
    @ManyToOne
    @JoinColumn(name = "dipartimento_id")
    private Dipartimento dipartimento;

    // 🔁 Relazione follow
    @ManyToMany(mappedBy = "seguiti")
    private Set<AppUser> follower = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_follow",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "seguito_id")
    )
    private Set<AppUser> seguiti = new HashSet<>();

    // ✅ Evita loop infiniti con Hibernate e Lombok
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser other = (AppUser) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}